package meeter.app.test;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class EventDatabase {
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS events (
                id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                category TEXT NOT NULL
            )
        """);
    }


    private final RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setName(rs.getString("name"));
        event.setCategory(rs.getString("category"));
        return event;
    };

    // Constructor
    public EventDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Return a list of events
    public List<Event> listEvents() {
        return jdbcTemplate.query("SELECT * FROM events", eventRowMapper);
    }

    // Return an event by id
    public Optional<Event> listSingle(Long id) {
        List<Event> events = jdbcTemplate.query(
        "SELECT * FROM events WHERE id = ?", 
        eventRowMapper, 
        id
    );
        return events.stream().findFirst();
    }

    public Event addEvent(Event event) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO events (name, category) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, event.getName());
            ps.setString(2, event.getCategory());
            return ps;
        }, keyHolder);
        
        event.setId(keyHolder.getKey().longValue());
        return event;
    }

    public Event updateEvent(Event event) {
        jdbcTemplate.update(
            "UPDATE events SET name = ?, category = ? WHERE id = ?",
            event.getName(),
            event.getCategory(),
            event.getId()
        );
        return event;
    }

    public boolean deleteEvent(Long id) {
        return jdbcTemplate.update("DELETE FROM events WHERE id = ?", id) > 0;
    }

    public void exampleQuery() {
        String sql = "SELECT * FROM your_table";
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            // Process the ResultSet here
            return null;
        });
    }
}
    