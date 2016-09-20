package com.qa.data.visualization.util;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.ajax.ColumnDef;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class QueryUtils {
    private EntityManager entityManager;
    private Long totalCount = 0L;

    public QueryUtils(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static StringBuilder getFilterQuery(DatatablesCriterias criterias) {
        StringBuilder queryBuilder = new StringBuilder();
        List<String> paramList = new ArrayList<String>();

        /**
         * Step 1.1: global filtering
         */
        if (StringUtils.isNotBlank(criterias.getSearch()) && criterias.hasOneSearchableColumn()) {
            queryBuilder.append(" WHERE ");

            for (ColumnDef columnDef : criterias.getColumnDefs()) {
                if (columnDef.isSearchable() && StringUtils.isBlank(columnDef.getSearch())) {
                    paramList.add(" LOWER(p." + columnDef.getName()
                            + ") LIKE '%?%'".replace("?", criterias.getSearch().toLowerCase()));
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
                        paramList.add(" LOWER(p." + columnDef.getName()
                                + ") LIKE '%?%'".replace("?", columnDef.getSearch().toLowerCase()));
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

    public <T> List<T> getRecordsWithDatatablesCriterias(Class<T> entiteClass, DatatablesCriterias criterias) {
        totalCount = 0L;
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM " + entiteClass.getSimpleName() + " p");

        /**
         * Step 1: global and individual column filtering
         */
        queryBuilder.append(getFilterQuery(criterias));

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

        return query.getResultList();
    }

    /**
     * <p>
     * Query used to return the number of filtered persons.
     *
     * @param criterias The DataTables criterias used to filter the persons. (maxResult,
     *                  filtering, paging, ...)
     * @return the number of filtered persons.
     */
    public <T> Long getFilteredCount(Class<T> entiteClass, DatatablesCriterias criterias) {
        if (StringUtils.isBlank(criterias.getSearch()) && (!criterias.hasOneFilteredColumn())) {
            if (totalCount != 0L) {
                return totalCount;
            } else {
                return getTotalCount(entiteClass);
            }
        }
        javax.persistence.Query query = this.entityManager.createQuery("SELECT p FROM " + entiteClass.getSimpleName() + " p" + getFilterQuery(criterias));
        return Long.parseLong(String.valueOf(query.getResultList().size()));
    }

    /**
     * @return the total count of persons.
     */
    public <T> Long getTotalCount(Class<T> entiteClass) {
        javax.persistence.Query query = this.entityManager.createQuery("SELECT COUNT(id) FROM " + entiteClass.getSimpleName() + " p");
        totalCount = (Long) query.getSingleResult();
        return totalCount;
    }
}