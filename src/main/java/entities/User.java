package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.deleteAll", query = "delete from User u")
})
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String userName;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String userPass;
  @ManyToMany(cascade = CascadeType.REMOVE)
  @JoinTable(name = "user_roles", joinColumns = {
          @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
          @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  private List<Role> roleList = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Booking> bookings = new ArrayList<>();

  public List<Booking> getBookings() {
    return bookings;
  }

  public void addBookings(Booking booking) {
    this.bookings.add(booking);
  }

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
        rolesAsStrings.add(role.getRoleName());
      });
    return rolesAsStrings;
  }

  public User() {}

  //TODO Change when password is hashed
   public boolean verifyPassword(String pw){
    return BCrypt.checkpw(pw, userPass);
    }

  public User(String userName, String userPass) {
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
  }
  public User(String userName, String userPass, List<Role> roleList) {
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
    this.roleList = roleList;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());;
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }



}
