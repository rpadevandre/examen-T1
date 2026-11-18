Feature: Inscripción a cursos
  Como estudiante del instituto
  Quiero inscribirme en los cursos disponibles
  Para poder llevarlos en el ciclo
  (el estudiante I2024001 ya está inscrito en el curso 1)

  Scenario: Inscripción correcta
    Given un estudiante nuevo que no está inscrito en ningún curso
    When se inscribe en el curso 2
    Then el sistema responde con código 200
    And la inscripción devuelta tiene el código del estudiante

  Scenario: Inscripción duplicada
    Given el estudiante "I2024001" ya está inscrito en el curso 1
    When intenta inscribirse de nuevo en el curso 1
    Then el sistema rechaza la inscripción
