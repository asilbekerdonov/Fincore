package com.example.spring.core.transfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> , JpaSpecificationExecutor<Transfer> {


     static Specification<Transfer> byAccountId(Long accountId) {
        return (root, query, cb) -> {
            if (accountId == null) {
                return null;
            }
            return cb.or(
                    cb.equal(root.get("fromAccountId"), accountId),
                    cb.equal(root.get("toAccountId"), accountId)
            );
        };
    }

    static Specification<Transfer> hasStatus(Status status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

   static Specification<Transfer> amountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) ->
                minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }



}
