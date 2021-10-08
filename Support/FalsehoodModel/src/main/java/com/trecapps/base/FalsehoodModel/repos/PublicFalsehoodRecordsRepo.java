package com.trecapps.base.FalsehoodModel.repos;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.trecapps.base.FalsehoodModel.models.PublicFalsehoodRecords;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PublicFalsehoodRecordsRepo extends CosmosRepository<PublicFalsehoodRecords, BigInteger> {
}
