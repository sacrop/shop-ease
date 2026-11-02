package com.shop.ease.model;



import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "review")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @NotNull
    private String name;

    private String reviewDescription;

    @Email
    private String reviewerEmail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUserDetails user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String rating;

    @Column(name = "review_date")
    private LocalDate date;

    @Override
    public String toString() {
        return "Review [reviewId=" + reviewId + ", name=" + name + ", reviewDescription=" + reviewDescription + ", reviewerEmail=" + reviewerEmail
                + ", rating=" + rating + ", date=" + date + "]";
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public MyUserDetails getUser() {
        return user;
    }

    public void setUser(MyUserDetails user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    
}
