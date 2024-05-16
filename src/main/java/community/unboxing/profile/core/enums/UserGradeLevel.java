package community.unboxing.profile.core.enums;

public enum UserGradeLevel {
    
    ENTHUSIAST("Enthusiast"),
    PROFESSIONAL("Professional"),
    EXPERT("Expert");

    private final String gradeLevel;

    UserGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

}
