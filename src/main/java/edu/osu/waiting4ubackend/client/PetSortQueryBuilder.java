package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery;

import static edu.osu.waiting4ubackend.client.PetDBClient.PETS_COLLECTION_NAME;

public class PetSortQueryBuilder {
    private String sort;
    private String order;

    public PetSortQueryBuilder() {

    }

    public PetSortQueryBuilder setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public PetSortQueryBuilder setOrder(String order) {
        this.order = order;
        return this;
    }

    public Query<Entity> build() {
        StructuredQuery.OrderBy orderBy;
        if(sort.equals("age") && order.equals("asc")) {
            orderBy = StructuredQuery.OrderBy.asc("dateOfBirth");
        } else if(sort.equals("age") && order.equals("desc")) {
            orderBy = StructuredQuery.OrderBy.desc("dateOfBirth");
        } else if(sort.equals("date") && order.equals("asc")) {
            orderBy = StructuredQuery.OrderBy.asc("dateCreated");
        } else {
            orderBy = StructuredQuery.OrderBy.desc("dateCreated");
        }

        EntityQuery.Builder builder = Query.newEntityQueryBuilder()
                .setKind(PETS_COLLECTION_NAME);

        return builder.setOrderBy(orderBy).build();
    }
}
