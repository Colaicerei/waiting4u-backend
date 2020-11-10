package edu.osu.waiting4ubackend.client;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery;

import java.util.ArrayList;
import java.util.List;

import static edu.osu.waiting4ubackend.client.PetDBClient.PETS_COLLECTION_NAME;

public class PetSearchQueryBuilder {
    private String type;
    private String breed;
    private List<String> dispositions;

    public PetSearchQueryBuilder() {

    }

    public PetSearchQueryBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public PetSearchQueryBuilder setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public PetSearchQueryBuilder setDispositions(List<String> dispositions) {
        this.dispositions = dispositions;
        return this;
    }

    public Query<Entity> build() {
        List<StructuredQuery.Filter> list = new ArrayList<>();
        if(type != null) {
            list.add(StructuredQuery.PropertyFilter.eq("type", type));
        }
        if(breed != null) {
            list.add(StructuredQuery.PropertyFilter.eq("breed", breed));
        }
        if(dispositions != null) {
            if(dispositions.size() == 1) {
                list.add(StructuredQuery.PropertyFilter.eq("dispositions", dispositions.get(0)));
            } else if (dispositions.size() == 2) {
                list.add(StructuredQuery.PropertyFilter.eq("dispositions", dispositions.get(0)));
                list.add(StructuredQuery.PropertyFilter.eq("dispositions", dispositions.get(1)));
            } else {
                list.add(StructuredQuery.PropertyFilter.eq("dispositions", dispositions.get(0)));
                list.add(StructuredQuery.PropertyFilter.eq("dispositions", dispositions.get(1)));
                list.add(StructuredQuery.PropertyFilter.eq("dispositions", dispositions.get(2)));
            }
        }
        EntityQuery.Builder builder = Query.newEntityQueryBuilder()
                .setKind(PETS_COLLECTION_NAME);

        if(list.isEmpty()) {
            return builder.build();
        }

        return builder.setFilter(StructuredQuery.CompositeFilter.and(list.get(0), list.toArray(new StructuredQuery.Filter[0]))).build();
    }

}