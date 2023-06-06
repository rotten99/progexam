package dtos;

import entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDTO {

    private String roleName;
    private List<String> userList;


    public RoleDTO(Role role) {
        this.roleName = role.getRoleName();
        this.userList = role.getUserList().stream().map(u -> u.getUserName()).collect(Collectors.toList());
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
