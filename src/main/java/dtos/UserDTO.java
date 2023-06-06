package dtos;

import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private String userName;
    private String userPass;
    private List<RoleDTO> roleList = new ArrayList<>();

    public UserDTO(User user){
        this.userName = user.getUserName();
        this.userPass = user.getUserPass();
        this.roleList = user.getRoleList().stream().map(r -> new RoleDTO(r)).collect(Collectors.toList());

    }

    public static List<UserDTO> getDtos(List<User> persons) {
        return persons.stream().map(p -> new UserDTO(p)).collect(Collectors.toList());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public List<RoleDTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleDTO> roleList) {
        this.roleList = roleList;
    }

}
