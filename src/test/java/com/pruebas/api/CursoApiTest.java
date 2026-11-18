package com.pruebas.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//OJO: antes de correr esta clase hay que levantar ModeloT1PswApplication (puerto 8080)
public class CursoApiTest {
    //DATOS DEL ALUMNO
    private static final String ALUMNO = "Andre Lopez";
    private static final String CODIGO = "I202500637";

    //el DataLoader ya carga este curso con id 1
    private static final String NOMBRE_CURSO = "Pruebas de Software";

    @BeforeAll
    static void configurar(){
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    // ESCENARIO 1
    @Test
    @DisplayName("ESCENARIO 1: consultar todos los cursos")
    void consultarTodosLosCursos(){
        given()
                .when()
                .get("/api/cursos")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    // ESCENARIO 2
    @Test
    @DisplayName("ESCENARIO 2: consultar el curso con id 1")
    void consultarCursoConId1(){
        Response r = given()
                .when()
                .get("/api/cursos/{id}", 1)
                .then()
                .statusCode(200)
                .body("nombre", equalTo(NOMBRE_CURSO))
                .extract().response();

        double precio = r.jsonPath().getDouble("precio");
        System.out.println("Precio del curso: " + precio);
        assertTrue(precio > 0, "El precio debe ser mayor que cero");
    }
}
