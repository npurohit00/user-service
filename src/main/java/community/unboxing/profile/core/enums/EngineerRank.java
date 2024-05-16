package community.unboxing.profile.core.enums;

public enum EngineerRank {
    CADET("Cadet", "<5 years of experience"),
    LIEUTENANT("Lieutenant", "5+ years of experience"),
    CAPTAIN("Captain", "10+ years of experience"),
    MARSHALL("Marshall", "20+ years of experience");

    private final String rankName;
    private final String experienceLevel;

    EngineerRank(String rankName, String experienceLevel) {
        this.rankName = rankName;
        this.experienceLevel = experienceLevel;
    }

    public String getRankName() {
        return rankName;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }
}
