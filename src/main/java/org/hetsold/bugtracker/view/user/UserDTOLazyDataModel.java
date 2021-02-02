package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class UserDTOLazyDataModel extends LazyDataModel<UserDTO> {
    private UserService userService;
    private Contract contract;

    public UserDTOLazyDataModel(UserService userService) {
        this.userService = userService;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public List<UserDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) userService.getUsersCount(contract));
        return userService.getUsers(contract, first, pageSize);
    }

    @Override
    public UserDTO getRowData(String rowKey) {
        return userService.getUserDTOById(rowKey);
    }

    @Override
    public String getRowKey(UserDTO object) {
        return object.getUuid();
    }
}
