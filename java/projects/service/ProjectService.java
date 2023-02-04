package projects.service;

import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;

import projects.doa.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

	public class ProjectService {
	
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {

		return projectDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
		
		return projectDao.fetchAllProjects();
	}

	public Project fetchByProjectId(Integer projectId) {
		
		return projectDao.fetchByProjectId(projectId).orElseThrow(() 
				-> new NoSuchElementException("\nProject ID = " + projectId + " does not exist."));
	}

	public void modifyProjectDetails(Project project) {
		
		if (!projectDao.modifyProjectDetails(project)) {
			throw new DbException("\nProject with ID = " + project.getProjectId() + " does not exist.");
		}
	}

	public void deleteProject(Integer projectId) {
		
		if (!projectDao.deleteProject(projectId)) {
			throw new DbException("\nProject with ID = " + projectId + " does not exist.");
		}
	}

}
