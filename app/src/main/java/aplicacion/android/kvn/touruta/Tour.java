package aplicacion.android.kvn.touruta;

class Tour {
    private int tourId;
    private String tourName;
    private String tourDescription;
    private String tourCountry;
    private String tourDistance;
    private String tourDuration;
    private String tourNumCheckpoints;
    private String tourPicture;

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    private int pictureId;

    public Tour(String tourName, String tourDescription, String tourCountry, String tourDistance, String tourDuration, String tourNumCheckpoints, String tourPicture) {
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.tourCountry = tourCountry;
        this.tourDistance = tourDistance;
        this.tourDuration = tourDuration;
        this.tourNumCheckpoints = tourNumCheckpoints;
        this.tourPicture = tourPicture;
    }

    public Tour(String tourName, String tourDescription, String tourCountry, String tourDistance, String tourDuration, String tourNumCheckpoints, int pictureId) {
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.tourCountry = tourCountry;
        this.tourDistance = tourDistance;
        this.tourDuration = tourDuration;
        this.tourNumCheckpoints = tourNumCheckpoints;
        this.pictureId = pictureId;
    }


    public Tour(){}

    public int getTourId() {
        return tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public void setTourDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public String getTourCountry() {
        return tourCountry;
    }

    public void setTourCountry(String tourCountry) {
        this.tourCountry = tourCountry;
    }

    public String getTourDistance() {
        return tourDistance;
    }

    public void setTourDistance(String tourDistance) {
        this.tourDistance = tourDistance;
    }

    public String getTourDuration() {
        return tourDuration;
    }

    public void setTourDuration(String tourDuration) {
        this.tourDuration = tourDuration;
    }

    public String getTourNumCheckpoints() {
        return tourNumCheckpoints;
    }

    public void setTourNumCheckpoints(String tourNumCheckpoints) {
        this.tourNumCheckpoints = tourNumCheckpoints;
    }

    public String getTourPicture() {
        return tourPicture;
    }

    public void setTourPicture(String tourPicture) {
        this.tourPicture = tourPicture;
    }
}
