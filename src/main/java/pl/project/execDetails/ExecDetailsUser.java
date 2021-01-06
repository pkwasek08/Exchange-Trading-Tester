package pl.project.execDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.components.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecDetailsUser {
    private Integer exeTime;
    private Integer dbTime;
    private User user;
    private List<User> userList;
}
