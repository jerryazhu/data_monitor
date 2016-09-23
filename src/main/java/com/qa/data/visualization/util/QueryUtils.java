package com.qa.data.visualization.util;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class QueryUtils {
    private EntityManager entityManager;
    private Class entiteClass;
    private DatatablesCriterias criterias;
    private Long totalCount = 0L;
    private int displayRecordsLength = 0;

    public <T> QueryUtils(EntityManager entityManager, Class<T> entiteClass, DatatablesCriterias criterias) {
        this.entityManager = entityManager;
        this.entiteClass = entiteClass;
        this.criterias = criterias;
    }

    public <T> DataSet<T> getResultDataSet() {
        List<T> actions = getRecordsWithDatatablesCriterias();
        Long count = getTotalCount();
        Long countFiltered = getFilteredCount();
        return new DataSet<T>(actions, count, countFiltered);
    }

    public StringBuilder getFilterQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        List<String> paramList = new ArrayList<String>();

        /**
         * Step 1.1: global filtering
         */
        if (StringUtils.isNotBlank(criterias.getSearch()) && criterias.hasOneSearchableColumn()) {
            queryBuilder.append(" WHERE ");

            for (ColumnDef columnDef : criterias.getColumnDefs()) {
                if (columnDef.isSearchable() && StringUtils.isBlank(columnDef.getSearch())) {
                    paramList.add(" p." + columnDef.getName()
                            + " LIKE '%?%'".replace("?", criterias.getSearch()));
                }
            }

            Iterator<String> itr = paramList.iterator();
            while (itr.hasNext()) {
                queryBuilder.append(itr.next());
                if (itr.hasNext()) {
                    queryBuilder.append(" OR ");
                }
            }
        }

        /**
         * Step 1.2: individual column filtering
         */
        if (criterias.hasOneSearchableColumn() && criterias.hasOneFilteredColumn()) {
            paramList = new ArrayList<String>();

            if (!queryBuilder.toString().contains("WHERE")) {
                queryBuilder.append(" WHERE ");
            } else {
                queryBuilder.append(" AND ");
            }

            for (ColumnDef columnDef : criterias.getColumnDefs()) {
                if (columnDef.isSearchable()) {
                    if (StringUtils.isNotBlank(columnDef.getSearchFrom())) {
                        paramList.add("p." + columnDef.getName() + " >= " + columnDef.getSearchFrom());
                    }

                    if (StringUtils.isNotBlank(columnDef.getSearchTo())) {
                        paramList.add("p." + columnDef.getName() + " < " + columnDef.getSearchTo());
                    }

                    if (StringUtils.isNotBlank(columnDef.getSearch())) {
                        paramList.add(" p." + columnDef.getName()
                                + " LIKE '%?%'".replace("?", columnDef.getSearch()));
                    }
                }
            }

            Iterator<String> itr = paramList.iterator();
            while (itr.hasNext()) {
                queryBuilder.append(itr.next());
                if (itr.hasNext()) {
                    queryBuilder.append(" AND ");
                }
            }
        }

        return queryBuilder;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getRecordsWithDatatablesCriterias() {
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM " + entiteClass.getSimpleName() + " p");

        /**
         * Step 1: global and individual column filtering
         */
        queryBuilder.append(getFilterQuery());

        /**
         * Step 2: sorting
         */
        if (criterias.hasOneSortedColumn()) {
            List<String> orderParams = new ArrayList<String>();
            queryBuilder.append(" ORDER BY ");
            for (ColumnDef columnDef : criterias.getSortedColumnDefs()) {
                orderParams.add("p." + columnDef.getName() + " " + columnDef.getSortDirection());
            }

            Iterator<String> itr2 = orderParams.iterator();
            while (itr2.hasNext()) {
                queryBuilder.append(itr2.next());
                if (itr2.hasNext()) {
                    queryBuilder.append(" , ");
                }
            }
        }

        TypedQuery<T> query = this.entityManager.createQuery(queryBuilder.toString(), entiteClass);

        /**
         * Step 3: paging
         */
        query.setFirstResult(criterias.getStart());
        query.setMaxResults(criterias.getLength());

        List<T> result = query.getResultList();
        displayRecordsLength = result.size();

        return result;
    }

    public Long getFilteredCount() {
        if (StringUtils.isBlank(criterias.getSearch()) && (!criterias.hasOneFilteredColumn())) {
            return totalCount;
        }
        if (criterias.getStart() == 0) {
            if (criterias.getLength() > displayRecordsLength) {
                return (long) displayRecordsLength;
            } else {
                return totalCount;
            }
        }
        javax.persistence.Query query = this.entityManager.createQuery("SELECT COUNT(id) FROM " + entiteClass.getSimpleName() + " p" + getFilterQuery());
        return (Long) query.getSingleResult();
    }

    public Long getTotalCount() {
        javax.persistence.Query query = this.entityManager.createQuery("SELECT COUNT(id) FROM " + entiteClass.getSimpleName() + " p");
        totalCount = (Long) query.getSingleResult();
        return totalCount;
    }
}