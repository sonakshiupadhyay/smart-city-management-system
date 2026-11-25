package com.smartcity.dao;

import com.smartcity.exception.DatabaseException;
import com.smartcity.model.InfrastructureProject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InfrastructureDAO - Data Access Object for infrastructure projects.
 * 
 * @author Smart City Management System
 */
public class InfrastructureDAO extends AbstractDAO<InfrastructureProject> {

    private static final String INSERT = 
        "INSERT INTO infrastructure_projects (project_name, description, category, location, planned_by, status, budget, start_date, end_date, impact_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
        "UPDATE infrastructure_projects SET project_name = ?, description = ?, category = ?, location = ?, status = ?, budget = ?, spent_amount = ?, start_date = ?, end_date = ?, impact_score = ? WHERE project_id = ?";
    private static final String DELETE = "DELETE FROM infrastructure_projects WHERE project_id = ?";
    private static final String FIND_BY_ID = 
        "SELECT p.*, u.full_name as planner_name FROM infrastructure_projects p LEFT JOIN users u ON p.planned_by = u.user_id WHERE p.project_id = ?";
    private static final String FIND_ALL = 
        "SELECT p.*, u.full_name as planner_name FROM infrastructure_projects p LEFT JOIN users u ON p.planned_by = u.user_id ORDER BY p.created_at DESC";
    private static final String COUNT = "SELECT COUNT(*) FROM infrastructure_projects";
    private static final String FIND_BY_PLANNER = 
        "SELECT p.*, u.full_name as planner_name FROM infrastructure_projects p LEFT JOIN users u ON p.planned_by = u.user_id WHERE p.planned_by = ? ORDER BY p.created_at DESC";
    private static final String FIND_BY_STATUS = 
        "SELECT p.*, u.full_name as planner_name FROM infrastructure_projects p LEFT JOIN users u ON p.planned_by = u.user_id WHERE p.status = ? ORDER BY p.created_at DESC";

    @Override
    public int insert(InfrastructureProject project) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getCategory());
            stmt.setString(4, project.getLocation());
            stmt.setInt(5, project.getPlannedBy());
            stmt.setString(6, project.getStatus());
            stmt.setDouble(7, project.getBudget());
            stmt.setDate(8, project.getStartDate());
            stmt.setDate(9, project.getEndDate());
            stmt.setDouble(10, project.getImpactScore());
            
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new DatabaseException("Creating project failed.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting project: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public boolean update(InfrastructureProject project) throws DatabaseException {
        return executeUpdate(UPDATE, project.getProjectName(), project.getDescription(),
            project.getCategory(), project.getLocation(), project.getStatus(),
            project.getBudget(), project.getSpentAmount(), project.getStartDate(),
            project.getEndDate(), project.getImpactScore(), project.getProjectId()) > 0;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        return executeUpdate(DELETE, id) > 0;
    }

    @Override
    public InfrastructureProject findById(int id) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapResultSet(rs);
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding project: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public List<InfrastructureProject> findAll() throws DatabaseException {
        List<InfrastructureProject> projects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_ALL);
            rs = stmt.executeQuery();
            while (rs.next()) projects.add(mapResultSet(rs));
            return projects;
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching projects: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    @Override
    public int count() throws DatabaseException {
        return executeCount(COUNT);
    }

    public List<InfrastructureProject> findByPlanner(int plannerId) throws DatabaseException {
        List<InfrastructureProject> projects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_PLANNER);
            stmt.setInt(1, plannerId);
            rs = stmt.executeQuery();
            while (rs.next()) projects.add(mapResultSet(rs));
            return projects;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding projects by planner: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    public List<InfrastructureProject> findByStatus(String status) throws DatabaseException {
        List<InfrastructureProject> projects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_STATUS);
            stmt.setString(1, status);
            rs = stmt.executeQuery();
            while (rs.next()) projects.add(mapResultSet(rs));
            return projects;
        } catch (SQLException e) {
            throw new DatabaseException("Error finding projects by status: " + e.getMessage(), e);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    private InfrastructureProject mapResultSet(ResultSet rs) throws SQLException {
        InfrastructureProject project = new InfrastructureProject();
        project.setProjectId(rs.getInt("project_id"));
        project.setProjectName(rs.getString("project_name"));
        project.setDescription(rs.getString("description"));
        project.setCategory(rs.getString("category"));
        project.setLocation(rs.getString("location"));
        project.setPlannedBy(rs.getInt("planned_by"));
        project.setStatus(rs.getString("status"));
        project.setBudget(rs.getDouble("budget"));
        project.setSpentAmount(rs.getDouble("spent_amount"));
        project.setStartDate(rs.getDate("start_date"));
        project.setEndDate(rs.getDate("end_date"));
        project.setImpactScore(rs.getDouble("impact_score"));
        project.setCreatedAt(rs.getTimestamp("created_at"));
        project.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { project.setPlannerName(rs.getString("planner_name")); } catch (SQLException e) { /* column may not exist */ }
        return project;
    }
}
