package com.pruebas.bdd.steps;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class InscripcionStepDefinitions {
    private static final String ALUMNO = "Andre Lopez";
    private static final String CODIGO = "I202500637";

    private String codigoEstudiante;
    private Response respuesta;

    @Before
    public void configurar(){
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    //metodo para mandar el POST y no repetir codigo
    private Response inscribir(String codigo, int cursoId){
        String body = """
                {"codigoEstudiante": "%s", "cursoId": %d}
                """.formatted(codigo, cursoId);

        return given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/inscripciones");
    }

    // ===== ESCENARIO 1 =====
    @Given("un estudiante nuevo que no está inscrito en ningún curso")
    public void unEstudianteNuevo(){
        //uso la hora para que el codigo sea distinto cada vez que lo corro
        codigoEstudiante = "E" + System.currentTimeMillis();
        System.out.println("Codigo generado: " + codigoEstudiante);
    }

    @When("se inscribe en el curso {int}")
    public void seInscribeEnElCurso(int cursoId){
        respuesta = inscribir(codigoEstudiante, cursoId);
    }

    @Then("el sistema responde con código {int}")
    public void elSistemaRespondeConCodigo(int codigo){
        assertEquals(codigo, respuesta.statusCode());
    }

    @And("la inscripción devuelta tiene el código del estudiante")
    public void laInscripcionTieneElCodigo(){
        assertEquals(codigoEstudiante, respuesta.jsonPath().getString("codigoEstudiante"));
    }

    // ===== ESCENARIO 2 =====
    @Given("el estudiante {string} ya está inscrito en el curso {int}")
    public void elEstudianteYaEstaInscrito(String codigo, int cursoId){
        //este dato ya lo carga el DataLoader, solo lo guardo
        codigoEstudiante = codigo;
    }

    @When("intenta inscribirse de nuevo en el curso {int}")
    public void intentaInscribirseDeNuevo(int cursoId){
        respuesta = inscribir(codigoEstudiante, cursoId);
    }

    @Then("el sistema rechaza la inscripción")
    public void elSistemaRechaza(){
        assertTrue(respuesta.statusCode() >= 400, "Se esperaba un error, pero llegó " + respuesta.statusCode());
    }
}
