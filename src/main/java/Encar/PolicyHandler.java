package Encar;

import Encar.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired RentRepository rentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentAccepted_StatusChange(@Payload RentAccepted rentAccepted){

        if(rentAccepted.isMe()){
            System.out.println("##### listener StatusChange : " + rentAccepted.toJson());

            Optional<Rent> optionalRent = rentRepository.findById(rentAccepted.getRentId());
            Rent rent = optionalRent.orElseGet(Rent::new);
            //Rent rent = new Rent() ;
            rent.setStatus(rentAccepted.getStatus());
            rentRepository.save(rent);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentCanceled_StatusChange(@Payload RentCanceled rentCanceled){

        if(rentCanceled.isMe()){
            System.out.println("##### listener StatusChange : " + rentCanceled.toJson());
            Optional<Rent> optionalRent = rentRepository.findById(rentCanceled.getRentId());
            Rent rent = optionalRent.orElseGet(Rent::new);
            //Rent rent = new Rent() ;
            rent.setRentId(rentCanceled.getRentId());
            rent.setStatus(rentCanceled.getStatus());
            rentRepository.save(rent);
        }
    }

}
