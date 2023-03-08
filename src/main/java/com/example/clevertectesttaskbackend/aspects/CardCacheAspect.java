package com.example.clevertectesttaskbackend.aspects;

import com.example.clevertectesttaskbackend.cache.Cache;
import com.example.clevertectesttaskbackend.entity.DiscountCard;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class CardCacheAspect {

    private final Cache cache;

    @Autowired
    public CardCacheAspect(@Qualifier("cardLruCache") Cache cardLruCache) {
        this.cache = cardLruCache;
    }

    @Around("execution(* com.example.clevertectesttaskbackend.dao.CardDao.findById(..))")
    public Object findById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long objectId = (Long) args[0];

        DiscountCard cacheCard = (DiscountCard) cache.getById(objectId);
        DiscountCard card = (DiscountCard) proceedFindById(joinPoint);
        if (cacheCard.equals(card)) {
            return Optional.of(cacheCard);
        } else {
            cache.put(card, 1);
        }
        return Optional.of(card);
    }

    public Object proceedFindById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Optional<DiscountCard> cardOptional = (Optional<DiscountCard>) result;
        return cardOptional.get();
    }

    @Around("execution(* com.example.clevertectesttaskbackend.dao.CardDao.save(..))")
    public Object save(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        cache.put(result, 1);
        return result;
    }

    @After("execution(* com.example.clevertectesttaskbackend.dao.CardDao.deleteById(..))")
    public void delete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long objectId = (Long) args[0];

        DiscountCard card = (DiscountCard) cache.getById(objectId);

        if (cache.size() != 0) {
            cache.evict(card);
        }
    }
}
