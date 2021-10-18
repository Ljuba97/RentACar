package com.rentACar.controller;

import com.rentACar.dao.ContractDao;
import com.rentACar.dao.ContractDaoSQL;
import com.rentACar.dao.UserDao;
import com.rentACar.dao.UserDaoSQL;
import com.rentACar.model.ContractModel;
import com.rentACar.model.request.ContractApprovalRequestModel;
import com.rentACar.model.request.ContractPendingRequestModel;
import com.rentACar.model.request.ContractSampleRequestModel;
import com.rentACar.model.response.ContractApprovalResponseModel;
import com.rentACar.model.response.ContractPendingResponseModel;
import com.rentACar.model.response.ContractSampleResponseModel;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ContractController {
    private static final ContractDao cd = new ContractDaoSQL();
    private static final UserDao ud = new UserDaoSQL();

    @PostMapping("/contracts/sample")
    public ContractSampleResponseModel sendSample(@RequestBody ContractSampleRequestModel sample) {
        return new ContractSampleResponseModel(sample.getUserId(), sample.getCarId(), sample.getStartDate(), sample.getEndDate(), cd.getCarPrice(sample.getCarId(), sample.getStartDate(), sample.getEndDate()), false);
    }

    @PostMapping("/contracts")
    public ContractPendingResponseModel addContract(@RequestBody ContractPendingRequestModel contract) {
        for (var x : cd.allContracts()) {
            if (x.getUserId().equals(contract.getUserId()) && !x.isApproved())
                return new ContractPendingResponseModel(false, "Contract not created. User already has a contract that is still not approved!");
        }
        for (Map.Entry<String, List<LocalDate>> e : cd.allBetweenDates().entrySet()) {
            for (var x : cd.betweenDates(contract.getStartDate(), contract.getEndDate())) {
                if (e.getValue().contains(x) && e.getKey().equals(contract.getContractId().toString()))
                    return new ContractPendingResponseModel(false, "Contract not created. Car not available!");
            }
        }

        cd.addContract(contract);
        return new ContractPendingResponseModel(true, "Contract successfully created. Waiting for approval!");
    }

    @GetMapping("/contracts")
    public List<ContractModel> allContracts(@RequestHeader(value = "authorization") UUID adminId) {
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId))
                return cd.allContracts();
        }
        return null;
    }

    @GetMapping("/contracts/pending")
    public List<ContractModel> allPendingContracts(@RequestHeader(value = "authorization") UUID adminId) {
        List<ContractModel> list = new ArrayList<>();
        for (var x : cd.allContracts()) {
            if (!x.isApproved())
                list.add(x);
        }
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId))
                return list;
        }
        return null;
    }

    @PostMapping("/contracts/{contractId}/approval")
    public ContractApprovalResponseModel contractApproval(@RequestHeader(value = "authorization") UUID adminId, @PathVariable("contractId") UUID contractId, @RequestBody ContractApprovalRequestModel contract) {
        boolean isAdmin = false;
        for (var x : ud.getAllAdminId()) {
            if (x.equals(adminId)) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            for (var x : cd.allContracts()) {
                if (x.getContractId().equals(contractId)) {
                    if (contract.isApproved()) {
                        cd.approveContract(contractId);
                        return new ContractApprovalResponseModel(true, "Contract is approved!");
                    }
                    else {
                        cd.deleteContract(contractId);
                        return new ContractApprovalResponseModel(true, "Contract is not approved!");
                    }
                }
            }

            return new ContractApprovalResponseModel(false, "Contract with this id doesn't exist!");
        }
        else
            return new ContractApprovalResponseModel(false, "Authorization problem!");
    }

    @GetMapping("contracts/{userId}/history")
    public List<ContractModel> userContractHistory(@PathVariable("userId") UUID userId) {
        return cd.allUserContracts(userId);
    }
}
