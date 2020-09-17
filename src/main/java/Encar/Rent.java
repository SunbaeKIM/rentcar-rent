package Encar;

import javax.persistence.*;

import org.springframework.beans.BeanUtils;

@Entity
@Table(name="Rent")
public class Rent {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long rentId;
    private Long carId;
    private String status;

    /**
     * 예약접수를 신청하면 상품수량을 확인하고 예약가능/불가여부를 판단
     * */
    @PrePersist
    public void onPrePersist(){
        //Tshop.external.Car car = new Tshop.external.Car();

        String checkQuantity = RentApplication.applicationContext.getBean(Encar.external.CarService.class).checkCarQuantity(this.getCarId().toString());

        if(Integer.parseInt(checkQuantity) > 0){
            this.setStatus("예약신청");
        }else{
            this.setStatus("예약불가");
        }
    }
    /**
     * 예약신청 가능이면 배정관리서비스로 예약번호 전송
     * */
    @PostPersist
    public void onPostPersist(){
        RentRequested rentRequested = new RentRequested();
        BeanUtils.copyProperties(this, rentRequested);
        if("예약신청".equals(this.getStatus())) rentRequested.publishAfterCommit();
    }
    /**
     * 예약취소 이 후 상품재고 원복 및 배정정보 삭제 이벤트 전달
     * */
    @PostUpdate
    public  void onPostUpdate(){
        if("예약취소".equals(this.getStatus())){
            RentCancelRequested rentCancelRequested = new RentCancelRequested();
            BeanUtils.copyProperties(this, rentCancelRequested);
            rentCancelRequested.publishAfterCommit();
        }
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }




}
