package br.com.meli.fresh.connectors;

import br.com.meli.fresh.dto.request.UserEmailDataDTO;
import br.com.meli.fresh.dto.response.UserResponseDTO;
import br.com.meli.fresh.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;



public class RestClient {

    private static final String EMAILAPIBASEURL = "http://localhost:8082";
    private static final String BUYERREGISTRATIONENDPOINT = "/buyer-registration-notify";
    private static final String SELLERREGISTRATIONENDPOINT = "/seller-registration-notify";
    private static final String BUYERACTIONENDPOINT = "/buyer-action-notify";
    private static final String SELLERACTIONENDPOINT = "/seller-action-notify";

    static RestTemplate restTemplate = new RestTemplate();

//    public static void main(String[] args) {
//        NotifyBuyAction();
//    }
    public static void NotifyBuyAction(User user){

        UserEmailDataDTO buyer = new UserEmailDataDTO();
        buyer.setOwnerRef(user.getId());
        buyer.setName(user.getName());
        buyer.setEmailTo(user.getEmail());

//        buyer.setOwnerRef("1");
//        buyer.setName("Teste com Name");
//        buyer.setEmailTo("luizfelipers19@gmail.com");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> result = restTemplate.postForEntity(EMAILAPIBASEURL + BUYERACTIONENDPOINT, buyer, String.class);
    }

    public static void NotifySellAction(){
        UserEmailDataDTO seller = new UserEmailDataDTO();
//        buyer.setOwnerRef(userResponseDTO.getId());
//        buyer.setName(userResponseDTO.getName());
//        buyer.setEmailTo(userResponseDTO.getEmail());

        seller.setOwnerRef("1");
        seller.setName("Teste com Name");
        seller.setEmailTo("luizfelipers19@gmail.com");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> result = restTemplate.postForEntity(EMAILAPIBASEURL + SELLERACTIONENDPOINT, seller, String.class);

    }

    public static void NotifyBuyerRegistration(UserResponseDTO userResponseDTO){
        UserEmailDataDTO buyer = new UserEmailDataDTO();
        buyer.setOwnerRef(userResponseDTO.getId());
        buyer.setName(userResponseDTO.getName());
        buyer.setEmailTo(userResponseDTO.getEmail());

//        buyer.setOwnerRef("1");
//        buyer.setName("Teste com Name");
//        buyer.setEmailTo("luizfelipers19@gmail.com");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        //HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> result = restTemplate.postForEntity(EMAILAPIBASEURL + BUYERREGISTRATIONENDPOINT, buyer, String.class);
    }

    public static void NotifySellerRegistration(UserResponseDTO userResponseDTO){
        UserEmailDataDTO seller = new UserEmailDataDTO();
        seller.setOwnerRef(userResponseDTO.getId());
        seller.setName(userResponseDTO.getName());
        seller.setEmailTo(userResponseDTO.getEmail());

//        seller.setOwnerRef("1");
//        seller.setName("Teste com Name");
//        seller.setEmailTo("luizfelipers19@gmail.com");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        //HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> result = restTemplate.postForEntity(EMAILAPIBASEURL + SELLERREGISTRATIONENDPOINT, seller, String.class);
    }

}
