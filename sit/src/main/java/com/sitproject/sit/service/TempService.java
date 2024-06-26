package com.sitproject.sit.service;
import com.sitproject.sit.dto.ResponseDTO;
import com.sitproject.sit.dto.TempDTO;
import com.sitproject.sit.entity.Temp;
import com.sitproject.sit.repository.TempRepository;
import com.sitproject.sit.util.varList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TempService {
    @Autowired
    private TempRepository tempRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

        public ResponseDTO saveTemp(TempDTO tempDTO) {
            ResponseDTO responseDTO = new ResponseDTO();
            try {
                Temp temp = new Temp(tempDTO.getDevice_id(), tempDTO.getTemperature(), tempDTO.getTimestamp());
                tempRepository.save(temp);
                System.out.println("Tracked Temperature to DB");
                messagingTemplate.convertAndSend("/topic/temperature", tempDTO);
                System.out.println("Sent Temperature to WebSocket");
                responseDTO.setCode(varList.RSP_SUCCES);
                responseDTO.setMessage("Success");
                responseDTO.setContent(tempDTO);
                responseDTO.setStatus(HttpStatus.ACCEPTED);
            }catch (Exception e) {
                responseDTO.setCode(varList.RSP_FAIL);
                responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                responseDTO.setMessage(e.getMessage());
                return responseDTO;
            }
            return responseDTO;
        }
}
