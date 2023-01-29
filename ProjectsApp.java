package projects;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;
import recipes.entity.Recipe;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();

	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project"
	);
	// @formatter:on
	private Object curProject;

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}

	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;

				case 1:
					createProject();
					break;
					
				case 2:
					listProjects();
					break;
					
				case 3:
					selectProject();
					break;

				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
				System.out.println();
			}
		}
	}

	private void selectProject() {
		listProjects();
		
		Integer projectId = getIntInput("\nEnter a project ID to select a project");
		
		curProject = null;
		
		curProject = projectService.fetchByProjectId(projectId);
			
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("     " + project.getProjectId() + ": " + project.getProjectName()));
	}

	private void createProject() {
		String projectName = getStringInput("\nEnter the project name");
		BigDecimal estimatedHours = getDecimalInput("\nEnter the estimated hours");
		BigDecimal actualHours = getDecimalInput("\nEnter the actual hours");
		Integer difficulty = getIntInput("\nEnter the project difficulty (1-5)");
		String notes = getStringInput("\nEnter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("\nYou have successfully created project: " + dbProject);
	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	private boolean exitMenu() {
		System.out.println("\nExiting the menu.");
		return true;
	}

	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("\nEnter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}

	private void printOperations() {
		System.out.println("These are the available selections. Press the Enter key to quit:");

		operations.forEach(line -> System.out.println("   " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");

		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}

}
