import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.text.SimpleDateFormat;

class Seat {
    int row;
    int col;
    String type;

    Seat(int row, int col, String type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Seat seat = (Seat) o;
        return row == seat.row &&
                col == seat.col &&
                type.equals(seat.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, type);
    }

}

class BookFlight {
    String userId, flightId, cardholdersName, cardNumber;
    int cost;
    ArrayList<Seat> Seats;

    BookFlight(String userId, String flightId, String cardholdersName, String cardNumber,
            int cost) {
        this.userId = userId;
        this.flightId = flightId;
        this.cardholdersName = cardholdersName;
        this.cardNumber = cardNumber;
        this.cost = cost;
    }
}

class AirlinePair {
    String airlineid;
    String modelName;

    AirlinePair(String airlineid, String modelName) {
        this.airlineid = airlineid;
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "ID : " + this.airlineid + " " + "Model: " + this.modelName;
    }
}

class Flight {
    String flightId;
    String airline;
    int airplaneId;
    String userid;
    int EconomyPrice;
    int BusinessPrice;
    String Departuredate;
    String departureFrom;
    String destination;
    String departuretime;
    int EconomyRow;
    int EconomyColumn;
    int BusinessRow;
    int BusinessColumn;

    Flight(int airplaneId, String userid, int EconomyPrice, int BusinessPrice, String Departuredate,
            String departuretime,
            String departureFrom, String destination) {
        this.airplaneId = airplaneId;
        this.userid = userid;
        this.EconomyPrice = EconomyPrice;
        this.BusinessPrice = BusinessPrice;
        this.Departuredate = Departuredate;
        this.departureFrom = departureFrom;
        this.destination = destination;
        this.departuretime = departuretime;

    }

    Flight(String flightId, String airline, String userid, String departuredate, String departuretime,
            String departureFrom, String destination) {
        this.flightId = flightId;
        this.airline = airline;
        this.userid = userid;
        this.Departuredate = departuredate;
        this.departuretime = departuretime;
        this.departureFrom = departureFrom;
        this.destination = destination;
    }
}

class Register {
    String username;
    String email;
    String password;
    String role;
    String gender;

    Register(String username, String email, String password, String role, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
    }
}

class Airplane {
    String airline;
    String userid;
    String model;
    int EconomyRow;
    int EconomyColumn;
    int BusinessRow;
    int BusinessColumn;

    Airplane(String airline, String userid, String model, int BusinessRow, int BusinessColumn, int EconomyRow,
            int EconomyColumn) {
        this.airline = airline;
        this.userid = userid;
        this.BusinessRow = BusinessRow;
        this.BusinessColumn = BusinessColumn;
        this.EconomyRow = EconomyRow;
        this.EconomyColumn = EconomyColumn;
        this.model = model;

    }
}

class User {
    String id;
    String username;
    String email;
    String role;

    public User(String id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}

class DB {
    static String url = "jdbc:mysql://localhost:3306/airline_db";
    static String userName = "root";
    static String password = "user";
    Connection connection;

    void createDbFlights() {
        try {
            String createFlightTable = "CREATE TABLE  IF NOT EXISTS Flights (flightId INT AUTO_INCREMENT PRIMARY KEY,userid INT NOT NULL, airplaneId INT NOT NULL,EconomyPrice INT NOT NULL, BusinessPrice INT NOT NULL,Departuredate DATE NOT NULL,Departuretime TIME NOT NULL, departureFrom varchar(50) NOT NULL,destination varchar(50) NOT NULL)";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(createFlightTable);
            if (result == 0) {
                System.out.println("Table Created Successfully");
            } else {
                System.out.println("Table not Created");
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    void createDbPayment() {
        try {
            String createUserTable = "CREATE TABLE  IF NOT EXISTS payments (paymentId INT AUTO_INCREMENT PRIMARY KEY, userId INT NOT NULL, cardholdersName VARCHAR(50) NOT NULL, cardNumber VARCHAR(50) NOT NULL,cost INT NOT NULL)";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(createUserTable);
            if (result == 0) {
                System.out.println("Table Created Successfully");
            } else {
                System.out.println("Table not Created");
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    void createDbBookFlight() {
        try {
            String createBookFlightsTable = "CREATE TABLE IF NOT EXISTS BookFlights (BookFlightId INT AUTO_INCREMENT PRIMARY KEY, userId INT NOT NULL, flightId INT NOT NULL, flightClassType ENUM('Business','Economy') NOT NULL, rowNumber INT NOT NULL, colNumber INT NOT NULL, paymentId INT NOT NULL)";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(createBookFlightsTable);
            if (result == 0) {
                System.out.println("Table Created Successfully");
            } else {
                System.out.println("Table not Created");
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    boolean insertBookFlight(BookFlight b) {
        try {
            // Insert payment information
            String paymentQuery = "INSERT INTO payments (userId, cardholdersName, cardNumber, cost) VALUES (?, ?, ?, ?)";
            PreparedStatement paymentStatement = connection.prepareStatement(paymentQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            paymentStatement.setString(1, b.userId);
            paymentStatement.setString(2, b.cardholdersName);
            paymentStatement.setString(3, b.cardNumber);
            paymentStatement.setInt(4, b.cost);
            int affectedRows = paymentStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting payment failed, no rows affected.");
            }

            // Retrieve generated payment ID
            int generatedPaymentId;
            try (ResultSet generatedKeys = paymentStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedPaymentId = generatedKeys.getInt(1);
                    System.out.println("Inserted Payment ID: " + generatedPaymentId);
                } else {
                    throw new SQLException("Inserting payment failed, no ID obtained.");
                }
            }

            // Insert booked flights
            for (Seat seat : b.Seats) {
                String searchSeatQuery = "SELECT COUNT(*) FROM BookFlights WHERE flightId = ? AND flightClassType = ? AND rowNumber = ? AND colNumber = ?";
                PreparedStatement searchSeatStatement = connection.prepareStatement(searchSeatQuery);
                searchSeatStatement.setString(1, b.flightId);
                searchSeatStatement.setString(2, seat.type);
                searchSeatStatement.setInt(3, seat.row);
                searchSeatStatement.setInt(4, seat.col);
                ResultSet searchResult = searchSeatStatement.executeQuery();
                if (searchResult.next()) {
                    int seatCount = searchResult.getInt(1);
                    if (seatCount == 0) {
                        String insertFlightQuery = "INSERT INTO BookFlights (userId, flightId, flightClassType, rowNumber, colNumber, paymentId) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement insertFlightStatement = connection.prepareStatement(insertFlightQuery);
                        insertFlightStatement.setString(1, b.userId);
                        insertFlightStatement.setString(2, b.flightId);
                        insertFlightStatement.setString(3, seat.type);
                        insertFlightStatement.setInt(4, seat.row);
                        insertFlightStatement.setInt(5, seat.col);
                        insertFlightStatement.setInt(6, generatedPaymentId);
                        int result = insertFlightStatement.executeUpdate();
                        if (result != 0) {
                            System.out.println("Inserted flight for seat: Row " + seat.row + ", Col " + seat.col);
                        } else {
                            System.err
                                    .println("Failed to insert flight for seat: Row " + seat.row + ", Col " + seat.col);
                        }
                    } else {
                        System.out.println("Seat already booked: Row " + seat.row + ", Col " + seat.col);
                    }
                }
            }
            return true;
        } catch (SQLException e) {

            System.out.println("Internal Error: " + e.getMessage());

        }
        return false;
    }

    boolean insertFlight(Flight f) {
        try {
            String insertFlightQuery = "INSERT INTO flights (airplaneId,userid,EconomyPrice,BusinessPrice,Departuredate,departureFrom,destination,Departuretime) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertFlightQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, f.airplaneId);
            statement.setString(2, f.userid);
            statement.setInt(3, f.EconomyPrice);
            statement.setInt(4, f.BusinessPrice);
            statement.setString(5, f.Departuredate);
            statement.setString(6, f.departureFrom);
            statement.setString(7, f.destination);
            statement.setString(8, f.departuretime);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting Plane failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Inserted Plane ID: " + generatedId);
                    return true;
                } else {
                    throw new SQLException("Inserting Plane failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
            return false;
        }
    }

    void createDbAirplane() {
        try {
            String createFlightTable = "CREATE TABLE IF NOT EXISTS airplanes (airplaneId INT AUTO_INCREMENT PRIMARY KEY, airline VARCHAR(50) NOT NULL, userid INT NOT NULL,model VARCHAR(50) NOT NULL, BusinessRow INT NOT NULL ,BusinessColumn INT NOT NULL, EconomyRow INT NOT NULL ,EconomyColumn INT NOT NULL)";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(createFlightTable);
            if (result == 0) {
                System.out.println("Table Created Successfully");
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    void createDbUsers() {
        try {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(50) NOT NULL, role ENUM('User','Admin') NOT NULL, DOB VARCHAR(20), country VARCHAR(20), phoneno VARCHAR(20), Gender ENUM('Male','Female') NOT NULL)";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(createUserTable);
            if (result == 0) {
                System.out.println("Table Created Successfully");
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    User login(String email, String password) {
        try {
            String searchUser = "select id,username,email,role from users where email=? and password=?";
            PreparedStatement statement = connection.prepareStatement(searchUser);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet userdetail = statement.executeQuery();
            if (userdetail.next()) {
                return new User(userdetail.getString(1), userdetail.getString(2), userdetail.getString(3),
                        userdetail.getString(4));
            }
            return new User("", "", "", "");
        } catch (Exception e) {
            return new User("", "", "", "");
        }
    }

    boolean registerUser(Register user) {
        try {
            String insertUser = "INSERT INTO users(username, email, password, role, Gender) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertUser);
            statement.setString(1, user.username);
            statement.setString(2, user.email);
            statement.setString(3, user.password);
            statement.setString(4, user.role);
            statement.setString(5, user.gender);
            int result = statement.executeUpdate();
            if (result == 1) {
                System.out.println("Registered Successfully");
                return true;
            } else {

                System.out.println("Not Registered");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
            return false;
        }
    }

    boolean insertAirplane(Airplane p) {
        try {
            String insertFlightQuery = "INSERT INTO airplanes (airline, userid,model,EconomyRow,EconomyColumn,BusinessRow,BusinessColumn) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertFlightQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, p.airline);
            statement.setString(2, p.userid);
            statement.setString(3, p.model);
            statement.setInt(4, p.EconomyRow);
            statement.setInt(5, p.EconomyColumn);
            statement.setInt(6, p.BusinessRow);
            statement.setInt(7, p.BusinessColumn);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting Plane failed, no rows affected.");

            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Inserted Plane ID: " + generatedId);
                    return true;
                } else {
                    throw new SQLException("Inserting Plane failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Internal Error: " + e.getMessage());
            return false;
        }
    }

    ArrayList<AirlinePair> UserAirlines(String id) {
        ArrayList<AirlinePair> response = new ArrayList<AirlinePair>();
        try {
            PreparedStatement statement = connection
                    .prepareStatement("select airplaneId,model from airplanes where userid=? ");
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                response.add(new AirlinePair(rs.getString(1), rs.getString(2)));
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return response;
    }

    ArrayList<Flight> searchFlight(String filter) {
        ArrayList<Flight> res = new ArrayList<>();
        try {
            String query = filter.length() == 0
                    ? "SELECT * FROM (SELECT flightId,EconomyPrice, airline,airplanes.userid,airplanes.airplaneId, Departuredate, departureFrom, destination, Departuretime FROM flights LEFT JOIN airplanes ON airplanes.airplaneId = flights.airplaneId ) AS f WHERE (f.departureDate > ? OR (f.departureDate = ? AND f.departureTime > ? ))"
                    : "SELECT * FROM (SELECT flightId, EconomyPrice,airline,airplanes.userid,airplanes.airplaneId, Departuredate, departureFrom, destination, Departuretime FROM flights LEFT JOIN airplanes ON airplanes.airplaneId = flights.airplaneId ) AS f WHERE (f.departureDate > ? OR (f.departureDate = ? AND f.departureTime > ? )) AND (f.departureFrom=? or f.destination=? or f.airline=?)";
            PreparedStatement statement = connection.prepareStatement(query);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
            String currDate = dateFormat.format(new Date());

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String currTime = timeFormat.format(new Date());
            statement.setString(1, currDate.toString());
            statement.setString(2, currDate.toString());
            statement.setString(3, currTime.toString());
            if (filter.length() != 0) {
                statement.setString(4, filter);
                statement.setString(5, filter);
                statement.setString(6, filter);
            }
            // statement.setString(3, from);
            // statement.setString(4, dest);
            ResultSet resultSet = statement.executeQuery();
            // System.out.println(resultSet.next());
            while (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("flightId"), resultSet.getString("airline"),
                        resultSet.getString("userid"), resultSet.getString("Departuredate"),
                        resultSet.getString("Departuretime"),
                        resultSet.getString("departureFrom"), resultSet.getString("destination"));
                flight.EconomyPrice = Integer.parseInt(resultSet.getString("EconomyPrice"));
                res.add(flight);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return res;
    }

    HashSet<Seat> getBookedFlightSeats(String flightId) {
        HashSet<Seat> res = new HashSet<>();

        try {
            PreparedStatement statement = connection.prepareStatement("select * from bookflights where flightId=?");
            statement.setString(1, flightId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                res.add(new Seat(Integer.parseInt(resultSet.getString("rowNumber")),
                        Integer.parseInt(resultSet.getString("colNumber")), resultSet.getString("flightClassType")));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return res;
    }

    Flight getFlightDetail(String flightId) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \n" +
                    "(SELECT flightId, airline,airplanes.userid, airplanes.airplaneId, Departuredate, departureFrom, destination, Departuretime,\n"
                    +
                    "EconomyPrice,BusinessPrice,EconomyRow,EconomyColumn,BusinessRow,BusinessColumn \n" +
                    "FROM flights INNER JOIN airplanes ON airplanes.airplaneId = flights.airplaneId AND  flights.flightId = ? ) as f;");
            statement.setString(1, flightId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(flightId + "ji" + resultSet.getString("flightId"));
                Flight f = new Flight(resultSet.getString("flightId"),
                        resultSet.getString("airline"),
                        resultSet.getString("userid"), resultSet.getString("Departuredate"),
                        resultSet.getString("Departuretime"),
                        resultSet.getString("departureFrom"), resultSet.getString("destination"));
                f.BusinessPrice = Integer.parseInt(resultSet.getString("BusinessPrice"));
                f.EconomyPrice = Integer.parseInt(resultSet.getString("EconomyPrice"));
                f.BusinessRow = Integer.parseInt(resultSet.getString("BusinessRow"));
                f.BusinessColumn = Integer.parseInt(resultSet.getString("BusinessColumn"));
                f.EconomyRow = Integer.parseInt(resultSet.getString("EconomyRow"));
                f.EconomyColumn = Integer.parseInt(resultSet.getString("EconomyColumn"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    DB() {
        try {

            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("connection is " + connection);
            createDbUsers();
            createDbAirplane();
            createDbFlights();
            createDbPayment();
            createDbBookFlight();
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
    }
}

public class Application {

    static DB sessionDB = new DB();

    public enum Page {
        HOME, LOGIN, REGISTER, ERROR, ADMIN, BOOK_FLIGHT
    }

    public enum STATE {
        LOGIN, NOT_LOGEDIN
    }

    public static String BOOK_FLIGHT_ID = null;
    public static User user = new User("", "", "", "User");
    public static Page currentPage = Page.HOME;
    public static STATE state = STATE.NOT_LOGEDIN;
    static JFrame app;

    public static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        switch (currentPage) {
            case HOME: {
                JMenu homeMenu = new JMenu("Page");
                if (state == STATE.NOT_LOGEDIN) {
                    JMenuItem loginMenuItem = new JMenuItem("Login");
                    JMenuItem registerMenuItem = new JMenuItem("Register");
                    loginMenuItem.addActionListener(e -> {
                        currentPage = Page.LOGIN;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    registerMenuItem.addActionListener(e -> {
                        currentPage = Page.REGISTER;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    homeMenu.add(loginMenuItem);
                    homeMenu.add(registerMenuItem);
                } else {
                    JMenuItem logOutMenuItem = new JMenuItem("Log Out");
                    logOutMenuItem.addActionListener(e -> {
                        currentPage = Page.HOME;
                        state = STATE.NOT_LOGEDIN;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    JMenuItem adminMenuItem = new JMenuItem("Admin");
                    adminMenuItem.addActionListener(e -> {
                        currentPage = Page.ADMIN;
                        // state = STATE.LOGIN;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    homeMenu.add(logOutMenuItem);
                    homeMenu.add(adminMenuItem);
                }
                menuBar.add(homeMenu);
                break;
            }
            case LOGIN: {
                JMenu homeMenu = new JMenu("Page");
                JMenuItem loginMenuItem = new JMenuItem("Login");
                JMenuItem registerMenuItem = new JMenuItem("Register");
                loginMenuItem.addActionListener(e -> {
                    currentPage = Page.LOGIN;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                registerMenuItem.addActionListener(e -> {
                    currentPage = Page.REGISTER;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                JMenuItem homeMenuItem = new JMenuItem("Home");
                homeMenuItem.addActionListener(e -> {
                    currentPage = Page.HOME;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                homeMenu.add(homeMenuItem);
                homeMenu.add(loginMenuItem);
                homeMenu.add(registerMenuItem);
                menuBar.add(homeMenu);
                break;
            }
            case REGISTER: {
                JMenu homeMenu = new JMenu("Page");
                JMenuItem loginMenuItem = new JMenuItem("Login");
                JMenuItem registerMenuItem = new JMenuItem("Register");
                loginMenuItem.addActionListener(e -> {
                    currentPage = Page.LOGIN;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                registerMenuItem.addActionListener(e -> {
                    currentPage = Page.REGISTER;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                JMenuItem homeMenuItem = new JMenuItem("Home");
                homeMenuItem.addActionListener(e -> {
                    currentPage = Page.HOME;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                homeMenu.add(homeMenuItem);
                homeMenu.add(loginMenuItem);
                homeMenu.add(registerMenuItem);
                menuBar.add(homeMenu);
                break;
            }
            case ADMIN: {
                JMenu homeMenu = new JMenu("Page");
                JMenuItem logOutMenuItem = new JMenuItem("Log Out");
                logOutMenuItem.addActionListener(e -> {
                    currentPage = Page.HOME;
                    state = STATE.NOT_LOGEDIN;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                JMenuItem homeMenuItem = new JMenuItem("Home");
                homeMenuItem.addActionListener(e -> {
                    currentPage = Page.HOME;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                homeMenu.add(homeMenuItem);
                homeMenu.add(logOutMenuItem);
                menuBar.add(homeMenu);
                break;
            }
            case BOOK_FLIGHT:
                JMenu homeMenu = new JMenu("Page");
                JMenuItem homeMenuItem = new JMenuItem("Home");
                homeMenuItem.addActionListener(e -> {
                    currentPage = Page.HOME;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                });
                homeMenu.add(homeMenuItem);
                if (state == STATE.NOT_LOGEDIN) {
                    JMenuItem loginMenuItem = new JMenuItem("Login");
                    JMenuItem registerMenuItem = new JMenuItem("Register");
                    loginMenuItem.addActionListener(e -> {
                        currentPage = Page.LOGIN;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    registerMenuItem.addActionListener(e -> {
                        currentPage = Page.REGISTER;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    homeMenu.add(loginMenuItem);
                    homeMenu.add(registerMenuItem);
                } else {
                    JMenuItem logOutMenuItem = new JMenuItem("Log Out");
                    logOutMenuItem.addActionListener(e -> {
                        currentPage = Page.HOME;
                        state = STATE.NOT_LOGEDIN;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    JMenuItem adminMenuItem = new JMenuItem("Admin");
                    adminMenuItem.addActionListener(e -> {
                        currentPage = Page.ADMIN;
                        // state = STATE.LOGIN;
                        JPanel panel = createComponents();
                        setContentPaneAndRefresh(app, panel);
                    });
                    homeMenu.add(logOutMenuItem);
                    homeMenu.add(adminMenuItem);
                }
                menuBar.add(homeMenu);
                break;
            default: {
                break;
            }
        }
        return menuBar;
    }

    public static JPanel createComponents() {
        JPanel component = new JPanel();
        JLabel label = null;

        switch (currentPage) {
            case HOME:
                component = createHomePage();
                break;
            case LOGIN:
                component = createLoginForm();
                break;
            case REGISTER:
                component = createRegisterForm();
                break;
            case ADMIN:
                component = createAdminPage();
                break;
            case BOOK_FLIGHT:
                component = createBookFlightPage();
                break;
            default:
                label = new JLabel("Error");
                break;
        }

        if (label != null) {
            component.add(label);
        }
        return component;
    }

    public static JPanel createBookFlightPage() {

        JPanel panel = new JPanel(new GridLayout(1, 2));
        JLabel costLabel = new JLabel("Total Cost:");
        System.out.println(BOOK_FLIGHT_ID + " hi");
        Flight f = sessionDB.getFlightDetail(BOOK_FLIGHT_ID);
        System.out.println(f + " hi");
        HashSet<Seat> st = sessionDB.getBookedFlightSeats(BOOK_FLIGHT_ID);

        st.forEach(e -> System.out.println(e.col + " " + e.row + " " + e.type));
        System.out.println(st.contains(new Seat(1, 3, "Business")));
        JLabel costField = new JLabel("0");
        // Panel for flight details
        JPanel flightDetail = new JPanel(new BorderLayout());
        flightDetail.setBorder(BorderFactory.createTitledBorder("Flight Details"));

        // Panel for seats selection
        JPanel seats = new JPanel(new BorderLayout());
        seats.setBorder(BorderFactory.createTitledBorder("Select Seats"));

        ArrayList<Seat> selectedSeats = new ArrayList<>();
        JPanel seatSelectionPanelBusiness = new JPanel(new GridLayout(f.BusinessRow, f.BusinessColumn, 10, 10));
        // Adding business class seats
        for (int i = 1; i <= f.BusinessRow; i++) {
            for (int j = 1; j <= f.BusinessColumn; j++) {
                int row = i;
                int column = j;
                JButton seatButton = new JButton(new ImageIcon(Application.class.getResource("seat.png")));
                seatButton.setBackground(new Color(255, 255, 255));
                seatButton.setForeground(new Color(0,0, 0));
                seatButton.setPreferredSize(new Dimension(30, 40)); // Adjusted size
                if (!st.contains(new Seat(row, column, "Business"))) {
                    seatButton.addActionListener(new ActionListener() {
                        boolean selected = false;

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            selected = !selected;
                            if (selected) {
                                seatButton.setBackground(Color.GREEN);
                                selectedSeats.add(new Seat(row, column, "Business"));
                                costField.setText(
                                        String.valueOf(Integer.parseInt(costField.getText()) + f.BusinessPrice));

                            } else {
                                seatButton.setBackground(null);
                                selectedSeats.removeIf(
                                        (seat) -> (seat.row == row && seat.col == column
                                                && seat.type.equals("Business")));
                                costField.setText(
                                        String.valueOf(Integer.parseInt(costField.getText()) - f.BusinessPrice));
                            }
                        }
                    });
                } else {
                    seatButton.setBackground(Color.RED);
                }
                seatSelectionPanelBusiness.add(seatButton);
            }
        }

        JPanel seatSelectionPanelEconomy = new JPanel(new GridLayout(f.EconomyRow, f.EconomyColumn, 10, 10));
        // Adding economy class seats
        for (int i = 1; i <= f.EconomyRow; i++) {
            for (int j = 1; j <= f.EconomyColumn; j++) {
                int row = i;
                int column = j;
                JButton seatButton = new JButton(new ImageIcon(Application.class.getResource("seat.png")));
                seatButton.setPreferredSize(new Dimension(30, 40)); // Adjusted size
                seatButton.setBackground(new Color(255, 255, 255));
                seatButton.setForeground(new Color(0,0, 0));
                if (!st.contains(new Seat(row, column, "Economy"))) {
                    seatButton.addActionListener(new ActionListener() {
                        boolean selected = false;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selected = !selected;
                            if (selected) {
                                seatButton.setBackground(Color.GREEN);
                                selectedSeats.add(new Seat(row, column, "Economy"));
                                costField.setText(
                                        String.valueOf(Integer.parseInt(costField.getText()) + f.EconomyPrice));
                            } else {
                                seatButton.setBackground(null);
                                selectedSeats.removeIf(
                                        (seat) -> (seat.row == row && seat.col == column
                                                && seat.type.equals("Economy")));
                                costField.setText(
                                        String.valueOf(Integer.parseInt(costField.getText()) - f.EconomyPrice));
                            }
                        }
                    });

                } else {
                    seatButton.setBackground(Color.RED);
                }
                seatSelectionPanelEconomy.add(seatButton);

            }
        }

        JScrollPane scrollPaneBusiness = new JScrollPane(seatSelectionPanelBusiness);
        JScrollPane scrollPaneEconomy = new JScrollPane(seatSelectionPanelEconomy);

        // Create a panel to hold both scroll panes and labels
        JPanel seatSelectionPanel = new JPanel();
        seatSelectionPanel.setLayout(new BoxLayout(seatSelectionPanel, BoxLayout.PAGE_AXIS));
        seatSelectionPanel.add(new JLabel("Business seats", SwingConstants.CENTER));
        seatSelectionPanel.add(scrollPaneBusiness);
        seatSelectionPanel.add(new JLabel("Economy seats", SwingConstants.CENTER));
        seatSelectionPanel.add(scrollPaneEconomy);

        seats.add(seatSelectionPanel, BorderLayout.CENTER);

        // Panel for payment
        JPanel payment = new JPanel(new BorderLayout());
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel cardholdersLabel = new JLabel("Cardholder's Name:");
        JTextField cardholdersField = new JTextField(15);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        JTextField cardNumberField = new JTextField(15);

        JButton payButton = new JButton("Pay");
        payButton.setBackground(new Color(0, 102, 102));
        payButton.setForeground(new Color(255, 255, 255));
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String CardholderName = cardNumberField.getText();
                String CardNumber = cardNumberField.getText();
                int TotalCost = Integer.parseInt(costField.getText());
                if (state == STATE.NOT_LOGEDIN) {
                    JOptionPane.showMessageDialog(payButton, "You must be logged in",
                            "WARNING", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (selectedSeats.size() == 0) {
                    JOptionPane.showMessageDialog(payButton, "Select Seat for Payment",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (CardholderName.length() == 0 || CardNumber.length() == 0) {
                    JOptionPane.showMessageDialog(payButton, "Enter valid numbers fields",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BookFlight bookFlight = new BookFlight(user.id, f.flightId, CardholderName, CardNumber, TotalCost);
                bookFlight.Seats = selectedSeats;

                Boolean response = sessionDB.insertBookFlight(bookFlight);
                if (response) {
                    JOptionPane.showMessageDialog(payButton, "Payment Sucessfull",
                            "Sucess", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    JOptionPane.showMessageDialog(payButton, "Payment not Sucessfull",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(new JLabel("Airline Name : " + f.airline), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(new JLabel("Departure From : " + f.departureFrom), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(new JLabel("Destination : " + f.destination), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(new JLabel("Date and Time : " + f.Departuredate + " " + f.departuretime), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(new JLabel("Business Price : " + f.BusinessPrice), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(new JLabel("Economy Price : " + f.EconomyPrice), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        paymentPanel.add(cardholdersLabel, gbc);

        gbc.gridx = 1;
        paymentPanel.add(cardholdersField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        paymentPanel.add(cardNumberLabel, gbc);

        gbc.gridx = 1;
        paymentPanel.add(cardNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        paymentPanel.add(costLabel, gbc);

        gbc.gridx = 1;
        paymentPanel.add(costField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        paymentPanel.add(payButton, gbc);

        payment.setBorder(BorderFactory.createTitledBorder("Payment"));
        payment.add(paymentPanel, BorderLayout.CENTER);

        flightDetail.add(seats, BorderLayout.CENTER);

        panel.add(flightDetail);
        panel.add(payment);

        return panel;
    }

    public static JPanel createAdminPage() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTabbedPane tp = new JTabbedPane();
        tp.add("Insert Airplane", createInsertAirplanePanel());
        tp.add("Insert Flight", createInsertFlightPanel());

        panel.add(tp, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel createInsertAirplanePanel() {
        JTextField airline;
        JTextField model;
        JTextField economyRow;
        JTextField economyColumn;
        JTextField businessRow;
        JTextField businessColumn;
        JPanel insertAirplane = new JPanel();
        insertAirplane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        insertAirplane.add(new JLabel("Airline:"), gbc);

        gbc.gridx = 1;
        airline = new JTextField(15);
        insertAirplane.add(airline, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        insertAirplane.add(new JLabel("Model:"), gbc);

        gbc.gridx = 1;
        model = new JTextField(15);
        insertAirplane.add(model, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        insertAirplane.add(new JLabel("Economy Row:"), gbc);

        gbc.gridx = 1;
        economyRow = new JTextField(15);
        insertAirplane.add(economyRow, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        insertAirplane.add(new JLabel("Economy Column:"), gbc);

        gbc.gridx = 1;
        economyColumn = new JTextField(15);
        insertAirplane.add(economyColumn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        insertAirplane.add(new JLabel("Business Row:"), gbc);

        gbc.gridx = 1;
        businessRow = new JTextField(15);
        insertAirplane.add(businessRow, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        insertAirplane.add(new JLabel("Business Column:"), gbc);

        gbc.gridx = 1;
        businessColumn = new JTextField(15);
        insertAirplane.add(businessColumn, gbc);

        JButton insertAirplaneButton = new JButton("Insert Airplane");
        gbc.gridx = 1;
        gbc.gridy = 6;
        insertAirplane.add(insertAirplaneButton, gbc);

        insertAirplaneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String airlineName = airline.getText();
                    String modelName = model.getText();

                    int economyRowNumber = 0;
                    int economyColumnNumber = 0;
                    int businessRowNumber = 0;
                    int businessColumnNumber = 0;

                    if (!economyRow.getText().isEmpty()) {
                        economyRowNumber = Integer.parseInt(economyRow.getText());
                    } else {
                        JOptionPane.showMessageDialog(insertAirplane, "Enter a valid number for Economy Row",
                                "Missing Information", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution if there's an error
                    }

                    if (!economyColumn.getText().isEmpty()) {
                        economyColumnNumber = Integer.parseInt(economyColumn.getText());
                    } else {
                        JOptionPane.showMessageDialog(insertAirplane, "Enter a valid number for Economy Column",
                                "Missing Information", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution if there's an error
                    }

                    if (!businessRow.getText().isEmpty()) {
                        businessRowNumber = Integer.parseInt(businessRow.getText());
                    } else {
                        JOptionPane.showMessageDialog(insertAirplane, "Enter a valid number for Business Row",
                                "Missing Information", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution if there's an error
                    }

                    if (!businessColumn.getText().isEmpty()) {
                        businessColumnNumber = Integer.parseInt(businessColumn.getText());
                    } else {
                        JOptionPane.showMessageDialog(insertAirplane, "Enter a valid number for Business Column",
                                "Missing Information", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution if there's an error
                    }

                    // If all inputs are valid, proceed with further processing
                    // Here, you can add your logic to handle the input data
                    System.out.println("Airline: " + airlineName);
                    System.out.println("Model: " + modelName);
                    System.out.println("Economy Row: " + economyRowNumber);
                    System.out.println("Economy Column: " + economyColumnNumber);
                    System.out.println("Business Row: " + businessRowNumber);
                    System.out.println("Business Column: " + businessColumnNumber);

                    Boolean response = sessionDB
                            .insertAirplane(new Airplane(airlineName, user.id, modelName, businessRowNumber,
                                    businessColumnNumber, economyRowNumber, economyColumnNumber));

                    if (response) {
                        JOptionPane.showMessageDialog(insertAirplane, "Airplane added successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(insertAirplane, "Failed to add airplane",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(insertAirplane, "Enter valid numbers for row and column fields",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });
        return insertAirplane;
    }

    public static JPanel createInsertFlightPanel() {
        JTextField economyPrice;
        JTextField businessPrice;
        JTextField departureDate;
        JTextField departureFrom;
        JTextField destination;
        JTextField departuretime;
        JPanel insertFlightPanel = new JPanel();
        insertFlightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        ArrayList<AirlinePair> list = sessionDB.UserAirlines(user.id);
        JComboBox<AirlinePair> airplaneId = new JComboBox<>(list.toArray(new AirlinePair[0]));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        insertFlightPanel.add(new JLabel("Airplane ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        insertFlightPanel.add(airplaneId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        insertFlightPanel.add(new JLabel("Economy Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        economyPrice = new JTextField(15);
        insertFlightPanel.add(economyPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        insertFlightPanel.add(new JLabel("Business Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        businessPrice = new JTextField(15);
        insertFlightPanel.add(businessPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        insertFlightPanel.add(new JLabel("Departure Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        departureDate = new JTextField(15);
        insertFlightPanel.add(departureDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        insertFlightPanel.add(new JLabel("Departure From:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        departureFrom = new JTextField(15);
        insertFlightPanel.add(departureFrom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        insertFlightPanel.add(new JLabel("Destination:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        destination = new JTextField(15);
        insertFlightPanel.add(destination, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        insertFlightPanel.add(new JLabel("Departuretime:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        departuretime = new JTextField(15);
        insertFlightPanel.add(departuretime, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        JButton insertFlightButton = new JButton("Insert Flight");

        insertFlightPanel.add(insertFlightButton, gbc);
        insertFlightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int AirplaneId = Integer.parseInt(list.get(airplaneId.getSelectedIndex()).airlineid);
                String userid = user.id;
                int EconomyPrice = 0;
                int BusinessPrice = 0;
                if (!economyPrice.getText().isEmpty()) {
                    EconomyPrice = Integer.parseInt(economyPrice.getText());
                } else {
                    JOptionPane.showMessageDialog(insertFlightPanel, "Enter a valid number for Economy Price",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!businessPrice.getText().isEmpty()) {
                    BusinessPrice = Integer.parseInt(businessPrice.getText());
                } else {
                    JOptionPane.showMessageDialog(insertFlightPanel, "Enter a valid number for Business Price",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String Departuredate = departureDate.getText();
                String Departuretime = departuretime.getText();
                String DepartureFrom = departureFrom.getText();
                String Destination = destination.getText();

                System.err.println("AirplaneId :" + AirplaneId);
                System.err.println("userid :" + userid);
                System.err.println("EconomyPrice :" + EconomyPrice);
                System.err.println("BusinessPrice :" + BusinessPrice);
                System.err.println("Departuredate :" + Departuredate);
                System.err.println("DepartureFrom :" + DepartureFrom);
                System.err.println("Destination :" + Destination);
                if (Departuredate.length() == 0 || DepartureFrom.length() == 0 || Destination.length() == 0
                        || Departuretime.length() == 0) {
                    JOptionPane.showMessageDialog(insertFlightPanel, "Please fill all required fields!",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean response = sessionDB.insertFlight(new Flight(AirplaneId, userid, EconomyPrice, BusinessPrice,
                        Departuredate, Departuretime, DepartureFrom, Destination));
                if (response) {
                    JOptionPane.showMessageDialog(insertFlightPanel, "Flight added successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(insertFlightPanel, "Failed to add Flight",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return insertFlightPanel;
    }
    private static JPanel showFlight(){
        JPanel jPanel2 = new JPanel(new GridLayout(0, 1)); 
        JScrollPane scrollPane = new JScrollPane(jPanel2);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return jPanel2;
    }
    private static JPanel createHomePage() {
        ArrayList<Flight> flights = sessionDB.searchFlight("");
        JPanel panel = new JPanel();
        JButton jButton1;
        JButton jButton2;
        JButton jButton3;
        JLabel jLabel1;

        JPanel jPanel1;
        JPanel jPanel2 = new JPanel(new GridLayout(0, 1)); 
        JScrollPane scrollPane = new JScrollPane(jPanel2);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel1 = new JPanel();
        JTextField jTextField1;

        jTextField1 = new JTextField();
        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setPreferredSize(new Dimension(800, 500));

        jLabel1.setText("Search");

        jButton1.setBackground(new Color(0, 102, 102));
        jButton1.setFont(new Font("Segoe UI", 1, 12));
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("search");

        jButton2.setBackground(new Color(0, 102, 102));
        jButton2.setFont(new Font("Segoe UI", 1, 12));
        jButton2.setForeground(new Color(255, 255, 255));
        jButton2.setText("Low to High");

        jButton3.setBackground(new Color(0, 102, 102));
        jButton3.setFont(new Font("Segoe UI", 1, 12));
        jButton3.setForeground(new Color(255, 255, 255));
        jButton3.setText("High to Low");

        for (Flight flight : flights) {
            JPanel jPanel4 = new JPanel();
            JLabel jLabel2 = new JLabel();
            JLabel jLabel3 = new JLabel();
            JLabel jLabel4 = new JLabel();
            JLabel jLabel5 = new JLabel();
            JLabel jLabel6 = new JLabel();
            JLabel jLabel7 = new JLabel();
            JButton jButton4 = new JButton();

            jPanel4.setBackground(new Color(255, 255, 255));

            jLabel2.setIcon(new ImageIcon(Application.class.getResource("airplane.png")));

            jLabel3.setText(flight.airline);

            jLabel4.setText(flight.departureFrom);

            jLabel5.setText(flight.departuretime);

            jLabel6.setText(flight.destination);

            jLabel7.setText("$" + flight.EconomyPrice);

            jButton4.setBackground(new Color(0, 102, 102));
            jButton4.setForeground(new Color(255, 255, 255));
            jButton4.setText("Book Now");

            jButton4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println(flight.flightId + " button is pressed");
                    currentPage = Page.BOOK_FLIGHT;
                    BOOK_FLIGHT_ID = flight.flightId;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);

                }
            });

            GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(14, 14, 14)
                                    .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 85,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel3)
                                    .addGap(40, 40, 40)
                                    .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 45,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addGap(38, 38, 38)
                                    .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 42,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addGap(40, 40, 40)
                                    .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 64,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addGap(29, 29, 29)
                                    .addComponent(jLabel7)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 22,
                                            Short.MAX_VALUE)
                                    .addComponent(jButton4)
                                    .addGap(22, 22, 22)));
            jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 83,
                                            GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 4, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(30, 30, 30)
                                    .addGroup(jPanel4Layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel5)
                                            .addComponent(jButton4))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

            jPanel2.add(jPanel4);
        }

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 282,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addGap(31, 31, 31)
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton3)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(34, Short.MAX_VALUE)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(31, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addGap(31, 31, 31)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(71, Short.MAX_VALUE)));

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.Alignment.TRAILING,
                                GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));


                                
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (jTextField1.getText().length() != 0) {
                            String searchQuery = jTextField1.getText();
                            ArrayList<Flight> searchResult = sessionDB.searchFlight(searchQuery);
                            flights.clear();
                            flights.addAll(searchResult);

                            // Remove existing components from jPanel2
                            jPanel2.removeAll();

                            for (Flight flight : flights) {
                                JPanel jPanel4 = new JPanel();
                                JLabel jLabel2 = new JLabel();
                                JLabel jLabel3 = new JLabel();
                                JLabel jLabel4 = new JLabel();
                                JLabel jLabel5 = new JLabel();
                                JLabel jLabel6 = new JLabel();
                                JLabel jLabel7 = new JLabel();
                                JButton jButton4 = new JButton();

                                jPanel4.setBackground(new Color(255, 255, 255));

                                jLabel2.setIcon(new ImageIcon(Application.class.getResource("airplane.png")));

                                jLabel3.setText(flight.airline);

                                jLabel4.setText(flight.departureFrom);

                                jLabel5.setText(flight.departuretime);

                                jLabel6.setText(flight.destination);

                                jLabel7.setText("$" + flight.EconomyPrice);

                                jButton4.setBackground(new Color(0, 102, 102));
                                jButton4.setForeground(new Color(255, 255, 255));
                                jButton4.setText("Book Now");

                                jButton4.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        System.out.println(flight.flightId + " button is pressed");
                                        currentPage = Page.BOOK_FLIGHT;
                                        BOOK_FLIGHT_ID = flight.flightId;
                                        JPanel panel = createComponents();
                                        setContentPaneAndRefresh(app, panel);
                                    }
                                });

                                GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
                                jPanel4.setLayout(jPanel4Layout);
                                jPanel4Layout.setHorizontalGroup(
                                        jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addGap(14, 14, 14)
                                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 85,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel3)
                                                        .addGap(40, 40, 40)
                                                        .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 45,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGap(38, 38, 38)
                                                        .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 42,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGap(40, 40, 40)
                                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 64,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGap(29, 29, 29)
                                                        .addComponent(jLabel7)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 22,
                                                                Short.MAX_VALUE)
                                                        .addComponent(jButton4)
                                                        .addGap(22, 22, 22)));
                                jPanel4Layout.setVerticalGroup(
                                        jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(GroupLayout.Alignment.TRAILING,
                                                        jPanel4Layout.createSequentialGroup()
                                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 83,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 4, Short.MAX_VALUE))
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addGap(30, 30, 30)
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel3)
                                                                .addComponent(jLabel4)
                                                                .addComponent(jLabel6)
                                                                .addComponent(jLabel7)
                                                                .addComponent(jLabel5)
                                                                .addComponent(jButton4))
                                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

                                jPanel2.add(jPanel4);
                            }
                            jPanel2.revalidate();
                            jPanel2.repaint();
                        }
                    }
                });
            }
        });

        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        System.err.println(jTextField1.getText());
                        ArrayList<Flight> searchResult = sessionDB.searchFlight(jTextField1.getText());
                        searchResult.sort((a, b) -> Double.compare(a.EconomyPrice, b.EconomyPrice));
                        flights.clear();
                        flights.addAll(searchResult);
                        jPanel2.removeAll();

                        for (Flight flight : flights) {
                            JPanel jPanel4 = new JPanel();
                            JLabel jLabel2 = new JLabel();
                            JLabel jLabel3 = new JLabel();
                            JLabel jLabel4 = new JLabel();
                            JLabel jLabel5 = new JLabel();
                            JLabel jLabel6 = new JLabel();
                            JLabel jLabel7 = new JLabel();
                            JButton jButton4 = new JButton();

                            jPanel4.setBackground(new Color(255, 255, 255));

                            jLabel2.setIcon(new ImageIcon(Application.class.getResource("airplane.png")));

                            jLabel3.setText(flight.airline);

                            jLabel4.setText(flight.departureFrom);

                            jLabel5.setText(flight.departuretime);

                            jLabel6.setText(flight.destination);

                            jLabel7.setText("$" + flight.EconomyPrice);

                            jButton4.setBackground(new Color(0, 102, 102));
                            jButton4.setForeground(new Color(255, 255, 255));
                            jButton4.setText("Book Now");

                            jButton4.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println(flight.flightId + " button is pressed");
                                    currentPage = Page.BOOK_FLIGHT;
                                    BOOK_FLIGHT_ID = flight.flightId;
                                    JPanel panel = createComponents();
                                    setContentPaneAndRefresh(app, panel);
                                }
                            });

                            GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
                            jPanel4.setLayout(jPanel4Layout);
                            jPanel4Layout.setHorizontalGroup(
                                    jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(14, 14, 14)
                                                    .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 85,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel3)
                                                    .addGap(40, 40, 40)
                                                    .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 45,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addGap(38, 38, 38)
                                                    .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 42,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addGap(40, 40, 40)
                                                    .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 64,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addGap(29, 29, 29)
                                                    .addComponent(jLabel7)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 22,
                                                            Short.MAX_VALUE)
                                                    .addComponent(jButton4)
                                                    .addGap(22, 22, 22)));
                            jPanel4Layout.setVerticalGroup(
                                    jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(GroupLayout.Alignment.TRAILING,
                                                    jPanel4Layout.createSequentialGroup()
                                                            .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 83,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 4, Short.MAX_VALUE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(30, 30, 30)
                                                    .addGroup(jPanel4Layout
                                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel3)
                                                            .addComponent(jLabel4)
                                                            .addComponent(jLabel6)
                                                            .addComponent(jLabel7)
                                                            .addComponent(jLabel5)
                                                            .addComponent(jButton4))
                                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

                            jPanel2.add(jPanel4);
                        }
                        jPanel2.revalidate();
                        jPanel2.repaint();

                    }

                });
            }
        });


        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        System.err.println(jTextField1.getText());
                        ArrayList<Flight> searchResult = sessionDB.searchFlight(jTextField1.getText());
                        searchResult.sort((a, b) -> Double.compare(b.EconomyPrice, a.EconomyPrice));
                        flights.clear();
                        flights.addAll(searchResult);
                        jPanel2.removeAll();

                        for (Flight flight : flights) {
                            JPanel jPanel4 = new JPanel();
                            JLabel jLabel2 = new JLabel();
                            JLabel jLabel3 = new JLabel();
                            JLabel jLabel4 = new JLabel();
                            JLabel jLabel5 = new JLabel();
                            JLabel jLabel6 = new JLabel();
                            JLabel jLabel7 = new JLabel();
                            JButton jButton4 = new JButton();

                            jPanel4.setBackground(new Color(255, 255, 255));

                            jLabel2.setIcon(new ImageIcon(Application.class.getResource("airplane.png")));

                            jLabel3.setText(flight.airline);

                            jLabel4.setText(flight.departureFrom);

                            jLabel5.setText(flight.departuretime);

                            jLabel6.setText(flight.destination);

                            jLabel7.setText("$" + flight.EconomyPrice);

                            jButton4.setBackground(new Color(0, 102, 102));
                            jButton4.setForeground(new Color(255, 255, 255));
                            jButton4.setText("Book Now");

                            jButton4.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println(flight.flightId + " button is pressed");
                                    currentPage = Page.BOOK_FLIGHT;
                                    BOOK_FLIGHT_ID = flight.flightId;
                                    JPanel panel = createComponents();
                                    setContentPaneAndRefresh(app, panel);
                                }
                            });

                            GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
                            jPanel4.setLayout(jPanel4Layout);
                            jPanel4Layout.setHorizontalGroup(
                                    jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(14, 14, 14)
                                                    .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 85,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel3)
                                                    .addGap(40, 40, 40)
                                                    .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 45,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addGap(38, 38, 38)
                                                    .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 42,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addGap(40, 40, 40)
                                                    .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 64,
                                                            GroupLayout.PREFERRED_SIZE)
                                                    .addGap(29, 29, 29)
                                                    .addComponent(jLabel7)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 22,
                                                            Short.MAX_VALUE)
                                                    .addComponent(jButton4)
                                                    .addGap(22, 22, 22)));
                            jPanel4Layout.setVerticalGroup(
                                    jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(GroupLayout.Alignment.TRAILING,
                                                    jPanel4Layout.createSequentialGroup()
                                                            .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 83,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 4, Short.MAX_VALUE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(30, 30, 30)
                                                    .addGroup(jPanel4Layout
                                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel3)
                                                            .addComponent(jLabel4)
                                                            .addComponent(jLabel6)
                                                            .addComponent(jLabel7)
                                                            .addComponent(jLabel5)
                                                            .addComponent(jButton4))
                                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

                            jPanel2.add(jPanel4);
                        }
                        jPanel2.revalidate();
                        jPanel2.repaint();

                    }

                });
            }
        });
        

        return panel;
    }

    private static JPanel createLoginForm() {
        javax.swing.JButton loginButton;
        javax.swing.JLabel jLabel1;
        javax.swing.JLabel jLabel2;
        javax.swing.JLabel jLabel3;
        javax.swing.JLabel jLabel4;
        javax.swing.JLabel jLabel5;
        javax.swing.JPanel jPanel1;
        javax.swing.JPanel panel;
        javax.swing.JPanel jPanel3;
        javax.swing.JPanel jPanel4;
        javax.swing.JTextField jTextField1;
        javax.swing.JTextField jTextField2;
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        loginButton = new javax.swing.JButton();
        panel = new javax.swing.JPanel();
        panel.setBackground(Color.WHITE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setIcon(new javax.swing.ImageIcon(Application.class.getResource("airplane.png")));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Flight Booking Sysyem");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap(96, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(94, 94, 94))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(jLabel1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addContainerGap(203, Short.MAX_VALUE)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Login");

        jLabel4.setText("Email");

        jLabel5.setText("Password");

        loginButton.setBackground(new java.awt.Color(0, 102, 102));
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(212, 212, 212)
                                                .addComponent(jLabel3))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(112, 112, 112)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jTextField1,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 227,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jTextField2,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 227,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(loginButton,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                227, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(126, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(jLabel3)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(loginButton)
                                .addContainerGap(134, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 456, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                        jPanel3Layout.createSequentialGroup()
                                                .addGap(0, 343, Short.MAX_VALUE)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 800, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 430, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = jTextField2.getText();
                String password = jTextField1.getText();
                System.out.println("email: " + email);
                System.out.println("Password: " + password);
                if (email.length() == 0 || password.length() == 0) {
                    JOptionPane.showMessageDialog(panel, "Please fill all required fields!",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                user = sessionDB.login(email, password);
                if (user.id.equals("")) {
                    JOptionPane.showMessageDialog(panel, "User not login successful. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    state = STATE.LOGIN;
                    currentPage = Page.HOME;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                }

            }
        });

        return panel;
    }

    private static JPanel createRegisterForm() {
        JPanel panel = new JPanel();
        javax.swing.JButton signup;
        javax.swing.JLabel jLabel1;
        javax.swing.JLabel jLabel2;
        javax.swing.JLabel jLabel3;
        javax.swing.JLabel jLabel4;
        javax.swing.JLabel jLabel5;
        javax.swing.JLabel jLabel6;
        javax.swing.JLabel jLabel7;
        javax.swing.JLabel jLabel8;
        javax.swing.JLabel jLabel9;
        javax.swing.JPanel jPanel1;
        javax.swing.JPanel jPanel2;
        javax.swing.JRadioButton jRadioButton1;
        javax.swing.JRadioButton jRadioButton2;
        javax.swing.JTextField userName;
        javax.swing.JTextField emailField;
        javax.swing.JTextField passwordFiled;
        javax.swing.JTextField confirmPasswordFiled;
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        userName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        signup = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        passwordFiled = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        confirmPasswordFiled = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        ButtonGroup role = new ButtonGroup();
        role.add(jRadioButton1);
        role.add(jRadioButton2);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Flight Booking Sysytem");

        jLabel2.setIcon(new javax.swing.ImageIcon(Application.class.getResource("airplane.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Sign Up");

        jLabel4.setText("Email");

        jLabel5.setText("Password");

        signup.setBackground(new java.awt.Color(0, 0, 0));
        signup.setForeground(new java.awt.Color(255, 255, 255));
        signup.setText("Sign up");

        jLabel7.setText("User name");

        jLabel8.setText("Confirm Password");

        jLabel9.setText("Role");

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Admin");

        jRadioButton2.setText("User");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(81, 81, 81)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel9)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(jLabel7)
                                                                .addComponent(userName)
                                                                .addComponent(jLabel5)
                                                                .addComponent(jLabel4)
                                                                .addComponent(emailField)
                                                                .addComponent(passwordFiled)
                                                                .addComponent(jLabel8)
                                                                .addComponent(confirmPasswordFiled)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        jPanel2Layout.createSequentialGroup()
                                                                                .addGroup(jPanel2Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(signup)
                                                                                        .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(
                                                                                                        jRadioButton2)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(
                                                                                                        jRadioButton1)))
                                                                                .addGap(67, 67, 67)))))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(150, 150, 150)
                                                .addComponent(jLabel3)))
                                .addContainerGap(168, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(49, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordFiled, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(confirmPasswordFiled, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton2)
                                        .addComponent(jRadioButton1))
                                .addGap(18, 18, 18)
                                .addComponent(signup)
                                .addGap(75, 75, 75)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(137, 137, 137)
                                                .addComponent(jLabel2))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(93, 93, 93)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 169,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100,
                                        Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(197, 197, 197))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE));

        signup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userName.getText();
                String email = emailField.getText();
                String password = new String(passwordFiled.getText());
                String confirmPassword = new String(confirmPasswordFiled.getText());
                String role = "User";
                String gender = "Male";
                if (jRadioButton1.isSelected()) {
                    role = "Admin";
                }
                

                System.out.println("Username: " + username);
                System.out.println("email: " + email);
                System.out.println("password: " + password);
                System.out.println("confirmPassword: " + confirmPassword);
                System.out.println("role: " + role);
                System.out.println("gender: " + gender);

                if (username.length() == 0 || email.length() == 0 || password.length() == 0
                        || confirmPassword.length() == 0) {
                    JOptionPane.showMessageDialog(panel, "Please fill all required fields!",
                            "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Passwords do not match!",
                            "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // sessionDB.createDbUsers();
                Register user = new Register(username, email, confirmPassword, role, gender);
                Boolean response = sessionDB.registerUser(user);
                if (response) {
                    JOptionPane.showMessageDialog(panel, "User registration successful",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    currentPage = Page.LOGIN;
                    JPanel panel = createComponents();
                    setContentPaneAndRefresh(app, panel);
                } else {
                    JOptionPane.showMessageDialog(panel, "User registration not successful. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        app = new JFrame("Airline Reservation System");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setJMenuBar(createMenuBar());
        JPanel panel = createComponents();
        app.add(panel);
        app.setSize(800, 500);
        app.setResizable(false);
        app.setVisible(true);
    }

    private static void setContentPaneAndRefresh(JFrame frame, JPanel panel) {
        frame.setContentPane(panel);
        frame.setJMenuBar(createMenuBar());
        frame.revalidate();
        frame.repaint();
    }
}
