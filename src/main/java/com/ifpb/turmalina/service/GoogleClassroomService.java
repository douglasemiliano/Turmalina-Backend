package com.ifpb.turmalina.service;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleClassroomService {
    private Classroom initializeClassroomClient(String accessToken) throws GeneralSecurityException, IOException {
        this.validarToken(accessToken);
        Credential credentials = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);

        return new Classroom.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credentials
        ).setApplicationName("Classroom App").build();
    }

    public void validarToken(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessToken);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        if (connection.getResponseCode() != 200) {
            throw new IOException("Token inválido");
        }
    }

    public List<Course> listCourses(String accessToken) throws GeneralSecurityException, IOException {
        Classroom service = initializeClassroomClient(accessToken);
        ListCoursesResponse response = service.courses().list().execute();
        service.courses().list().execute();
        return response.getCourses();
    }

    public List<Course> listarCursosOwnerIdDiferentThanMe(String userId, String accessToken) throws GeneralSecurityException, IOException {
        Classroom service = initializeClassroomClient(accessToken);
        ListCoursesResponse cursos = service.courses().list().execute();
        service.courses().list().execute();

        List<Course> cursosFiltrados = new ArrayList<>();

        for (Course curso : cursos.getCourses()) {

            System.err.println("Curso OwnerId: " + curso.getOwnerId() + " - UserId: " + userId);
            if(curso.getOwnerId().equals(userId)) {
                System.err.println(curso.getOwnerId());
            }

            if (curso.getOwnerId() != null && !curso.getOwnerId().equals(userId)) {
                cursosFiltrados.add(curso);
            }
        }

        return cursosFiltrados;
    }

    public List<CourseWork> listarAtividades(String courseId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListCourseWorkResponse courseWorkResponse = service.courses().courseWork().list(courseId).execute();

            return courseWorkResponse.getCourseWork();
        } catch (Exception e) {
            throw new IOException("Erro ao listar atividades: " + e.getMessage(), e);
        }
    }

    public List<CourseWork> listarAtividadesConcluidas(String courseId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            List<CourseWork> todasAtividades = service.courses().courseWork().list(courseId).execute().getCourseWork();

            if (todasAtividades == null) return new ArrayList<>();

            List<CourseWork> atividadesConcluidas = new ArrayList<>();

            for (CourseWork atividade : todasAtividades) {
                ListStudentSubmissionsResponse submissionsResponse = service
                        .courses()
                        .courseWork()
                        .studentSubmissions()
                        .list(courseId, atividade.getId())
                        .execute();

                List<StudentSubmission> submissions = submissionsResponse.getStudentSubmissions();

                boolean temAlgumaConcluida = submissions != null && submissions.stream()
                        .anyMatch(sub -> "TURNED_IN".equals(sub.getState()) || "RETURNED".equals(sub.getState()));

                if (temAlgumaConcluida) {
                    atividadesConcluidas.add(atividade);
                }
            }

            return atividadesConcluidas;
        } catch (Exception e) {
            throw new IOException("Erro ao listar atividades concluídas: " + e.getMessage(), e);
        }
    }
    public List<Student> listStudents(String courseId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListStudentsResponse response = service.courses().students().list(courseId).execute();
            List<Student> alunos = service.courses().students().list(courseId).execute().getStudents();
            System.err.println("alinoooos - - -  " + alunos);
            System.err.println(response);
            return response.getStudents();
        } catch (Exception e) {
            throw new IOException("Erro ao listar alunos: " + e.getMessage(), e);
        }
    }

    public List<StudentSubmission> listStudentSubmissions(String courseId, String assignmentId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            ListStudentSubmissionsResponse response = service.courses().courseWork().studentSubmissions().list(courseId, assignmentId).execute();
            return response.getStudentSubmissions();
        } catch (Exception e) {
            throw new IOException("Erro ao listar submissões de alunos: " + e.getMessage(), e);
        }
    }

    public UserProfile getUserProfile(String alunoId, String accessToken) throws GeneralSecurityException, IOException {
        try {
            Classroom service = initializeClassroomClient(accessToken);
            return service.userProfiles().get(alunoId).execute();
        } catch (Exception e) {
            throw new IOException("Erro ao obter o perfil do Google: " + e.getMessage(), e);
        }
    }

    public List<CourseWork> listarAtividadesComStatus(String courseId, String userId, String accessToken) throws GeneralSecurityException, IOException {
        Classroom service = initializeClassroomClient(accessToken);

        List<CourseWork> response = new ArrayList<>();

        List<CourseWork> courseWorkResponse = service.courses().courseWork().list(courseId).execute().getCourseWork();

        if (courseWorkResponse != null) {

            for (CourseWork courseWork : courseWorkResponse) {
                List<StudentSubmission> submissions = service.courses().courseWork().studentSubmissions().list(courseId, courseWork.getId()).execute().getStudentSubmissions();
                if(submissions != null) {
                    submissions.stream()
                            .filter(studentSubmission -> studentSubmission.getUserId().equals(userId))
                            .forEach(submission -> {
                                System.err.println(submission.getUserId());
                                courseWork.setState(submission.getState());
                                response.add(courseWork);
                                System.err.println(submission.getState());
                            });
                } else {
                    response.add(courseWork);
                }


            }
        }



        return response;
    }

}
