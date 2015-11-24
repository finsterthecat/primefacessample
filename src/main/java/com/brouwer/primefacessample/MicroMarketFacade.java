/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brouwer.primefacessample;

import com.brouwer.primefacessample.model.MicroMarket;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author tonybrouwer
 */
@Stateless
public class MicroMarketFacade extends AbstractFacade<MicroMarket> {

  @PersistenceContext(unitName = "com.brouwer_primefacessample_war_1.0-SNAPSHOTPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public MicroMarketFacade() {
    super(MicroMarket.class);
  }

}
