public class LiftRide {
    private final int skierId;
    private final int resortId;
    private final String seasonId;
    private final String dayId;
    private final int time;
    private final int liftId;

    public LiftRide (int skierId, int resortId, String seasonId, String dayId, int time, int liftId) {
        this.skierId = skierId;
        this.resortId = resortId;
        this.seasonId = seasonId;
        this.dayId = dayId;
        this.time = time;
        this.liftId = liftId;
    }

    public LiftRide (String json) throws IllegalArgumentException {
        boolean hasSkier = false, hasResort = false, hasSeason = false, hasDay = false, hasTime = false,
                hasLift = false;
        int tempSkier = 0, tempResort = 0, tempTime  = 0, tempLift = 0;
        String tempSeason = "", tempDay = "";
        json = json.replace("{","");
        json = json.replace("}","");
        String[] jsonArr = json.split(",");
        for (String s : jsonArr) {
            String[] sArr = s.split(":");
            if (sArr[0].equalsIgnoreCase("\"skierID\"")) {
                tempSkier = Integer.parseInt(sArr[1].replace("\"",""));
                hasSkier = true;
            } else if (sArr[0].equalsIgnoreCase("\"resortID\"")) {
                tempResort = Integer.parseInt(sArr[1].replace("\"", ""));
                hasResort = true;
            } else if (sArr[0].equalsIgnoreCase("\"seasonID\"")) {
                tempSeason = sArr[1].replace("\"", "");
                hasSeason = true;
            } else if (sArr[0].equalsIgnoreCase("\"dayID\"")) {
                tempDay = sArr[1].replace("\"", "");
                hasDay = true;
            } else if (sArr[0].equalsIgnoreCase("\"time\"")) {
                tempTime = Integer.parseInt(sArr[1].replace("\"", "").trim());
                hasTime = true;
            } else if (sArr[0].equalsIgnoreCase("\"liftID\"")) {
                tempLift = Integer.parseInt(sArr[1].replace("\"", "").trim());
                hasLift = true;
            }
        }
        if (hasSkier && hasResort && hasSeason && hasDay && hasTime && hasLift) {
            this.skierId = tempSkier;
            this.resortId = tempResort;
            this.seasonId = tempSeason;
            this.dayId = tempDay;
            this.time = tempTime;
            this.liftId = tempLift;
        } else {
            throw new IllegalArgumentException("Valid parameters not found");
        }
    }

    public int getSkierId() {
        return skierId;
    }

    public int getResortId() {
        return resortId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public String getDayId() {
        return dayId;
    }

    public int getWaitTime() {
        return time;
    }

    public int getLiftId() {
        return liftId;
    }
}
