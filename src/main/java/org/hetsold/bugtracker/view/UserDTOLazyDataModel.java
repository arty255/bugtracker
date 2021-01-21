package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

class UserDTOLazyDataModel extends LazyDataModel<UserDTO> {
    private UserService userService;

    public UserDTOLazyDataModel(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) userService.getUserCount());
        return userService.getUsers(first, pageSize);
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
