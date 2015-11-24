/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.brouwer.primefacessample;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Abstract Facade.
 *
 * @author tonybrouwer
 */
public abstract class AbstractFacade<T> {

  private final Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  /**
   * Find all rows.
   * 
   * @return List of all rows
   */
  public List<T> findAll() {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    @SuppressWarnings("unchecked")
    CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery();
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  /**
   * Find all rows in range.
   * @param range Array of two row numbers: from and to
   * @return List of all rows in range
   */
  public List<T> findRange(int[] range) {
    @SuppressWarnings("unchecked")
    javax.persistence.criteria.CriteriaQuery<T> cq
            = (CriteriaQuery<T>) getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    javax.persistence.TypedQuery<T> query = getEntityManager().createQuery(cq);
    query.setMaxResults(range[1] - range[0] + 1);
    query.setFirstResult(range[0]);
    return query.getResultList();
  }

  /**
   * Count of all rows.
   * 
   * @return count of all rows
   */
  public int count() {
    javax.persistence.criteria.CriteriaQuery<Long> cq
            = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
    javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
    cq.select(getEntityManager().getCriteriaBuilder().count(rt));
    javax.persistence.TypedQuery<Long> query = getEntityManager().createQuery(cq);
    return query.getSingleResult().intValue();
  }

}
