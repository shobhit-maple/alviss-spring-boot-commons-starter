//package com.alviss.commons.dao;
//
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import lombok.AllArgsConstructor;
//import org.springframework.data.jpa.domain.Specification;
//
//@AllArgsConstructor
//public class EntitySpecification<E> implements Specification<E> {
//
//  private final SearchCriteria criteria;
//
//  @Override
//  public Predicate toPredicate
//      (final Root<E> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
//
//    if (criteria.getOperation().equalsIgnoreCase(">")) {
//      return builder.greaterThanOrEqualTo(
//          root.<String> get(criteria.getKey()), criteria.getValue().toString());
//    }
//    else if (criteria.getOperation().equalsIgnoreCase("<")) {
//      return builder.lessThanOrEqualTo(
//          root.<String> get(criteria.getKey()), criteria.getValue().toString());
//    }
//    else if (criteria.getOperation().equalsIgnoreCase(":")) {
//      if (root.get(criteria.getKey()).getJavaType() == String.class) {
//        return builder.like(
//            root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
//      } else {
//        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
//      }
//    }
//    return null;
//  }
//}