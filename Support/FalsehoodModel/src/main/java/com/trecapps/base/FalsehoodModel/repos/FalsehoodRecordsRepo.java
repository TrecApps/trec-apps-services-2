package com.trecapps.base.FalsehoodModel.repos;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.trecapps.base.FalsehoodModel.models.FalsehoodRecords;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FalsehoodRecordsRepo extends CosmosRepository<FalsehoodRecords, BigInteger> {
}
