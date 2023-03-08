package com.example.clevertectesttaskbackend.aspects;

import com.example.clevertectesttaskbackend.cache.Cache;
import com.example.clevertectesttaskbackend.entity.Product;
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
public class ProductCacheAspect {

    private final Cache cache;

    @Autowired
    public ProductCacheAspect(@Qualifier("productLruCache") Cache productLruCache) {
        this.cache = productLruCache;
    }

    @Around("execution(* com.example.clevertectesttaskbackend.dao.ProductDao.findById(..))")
    public Object findById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long objectId = (Long) args[0];

        Product cacheProduct = (Product) cache.getById(objectId);
        Product product = (Product) proceedFindById(joinPoint);
        if (cacheProduct.equals(product)) {
            return Optional.of(cacheProduct);
        } else {
            cache.put(product, 1);
        }
        return Optional.of(product);
    }

    private Object proceedFindById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Optional<Product> productOptional = (Optional<Product>) result;
        return productOptional.get();
    }

    @Around("execution(* com.example.clevertectesttaskbackend.dao.ProductDao.save(..))")
    public Object save(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        cache.put(result, 1);
        return result;
    }

    @After("execution(* com.example.clevertectesttaskbackend.dao.ProductDao.deleteById(..))")
    public void delete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long objectId = (Long) args[0];

        Product product = (Product) cache.getById(objectId);

        if (cache.size() != 0) {
            cache.evict(product);
        }
    }
}
