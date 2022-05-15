package pl.edu.pw.restapi.dto;

public class CourseDTO {

    private Long id;
    private String title;
    private String thumbnail;
    private Double price;

    public CourseDTO(Long id, String title, String thumbnail, Double price) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.price = price;
    }

    public CourseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
