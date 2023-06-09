package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookItApiUtil {


    public static String generateToken(String email, String password) {
        Response response = given().
                accept(ContentType.JSON)
                .queryParam("email", email)
                .queryParam("password", password)
                .when()
                .get(Environment.BASE_URL + "/sign")
                .then().extract().response();

        String token = response.path("accessToken");

        return "Bearer " + token;
    }


    //one method param role --> userType "student-member" "student-leader" "teacher"
    //returns --> token


    public static void deleteStudent(String studentEmail, String studentPassword) {

        //1.send a get request to get token with student information
        String studentToken = BookItApiUtil.generateToken(studentEmail, studentPassword);

        //2.send a get request to /api/users/me endpoint and get the id number
        int idToDelete = given().accept(ContentType.JSON)
                .and().header("Authorization", studentToken)
                .when()
                .get(Environment.BASE_URL + "/api/users/me")
                .then().statusCode(200).extract().jsonPath().getInt("id");

        //3.send a delete request as a teacher to /api/students/{id} endpoint to delete the student
        String teacherToken = BookItApiUtil.generateToken(Environment.TEACHER_EMAIL, Environment.TEACHER_PASSWORD);
        given().
                pathParam("id", idToDelete)
                .and().
                header("Authorization", teacherToken)
                .when()
                .delete(Environment.BASE_URL + "/api/students/{id}")
                .then()
                .statusCode(204);
    }

    public static String getTokenByRole(String role) {
        String email, pass;

        switch (role) {

            case "teacher":
                email = Environment.TEACHER_EMAIL;
                pass = Environment.TEACHER_PASSWORD;
                break;
            case "student-member":
                email = Environment.MEMBER_EMAIL;
                pass = Environment.MEMBER_PASSWORD;
                break;

            case "student-leader":
                email = Environment.LEADER_EMAIL;
                pass = Environment.LEADER_PASSWORD;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + role);
        }

        return generateToken(email, pass);

    }
}
