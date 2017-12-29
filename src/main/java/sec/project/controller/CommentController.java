package sec.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {
    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String listComments(Model model) throws SQLException {
        String databaseAddress = "jdbc:h2:file:./database";

        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Comment");

        List<String> comments = new ArrayList<String>();

        while (resultSet.next()) {
            comments.add(resultSet.getString("content"));
        }

        connection.close();

        model.addAttribute("comments", comments);
        return "comments";
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public String submitForm(@RequestParam String comment) throws SQLException {
        String databaseAddress = "jdbc:h2:file:./database";

        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Comment (content) VALUES ('" + comment + "')");
        connection.close();

        return "redirect:/comments";
    }

    @ExceptionHandler(SQLException.class)
    public String databaseError(Model model, SQLException ex) {
        model.addAttribute("exception", ex);
        model.addAttribute("state", ex.getSQLState());

        return "databaseError";
    }
}
