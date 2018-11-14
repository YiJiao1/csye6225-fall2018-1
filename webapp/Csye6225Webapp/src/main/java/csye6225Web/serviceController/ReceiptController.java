package csye6225Web.serviceController;


import csye6225Web.models.Receipt;
import csye6225Web.models.Transaction;
import csye6225Web.repositories.ReceiptRepository;
import csye6225Web.repositories.TransactionRepository;
import csye6225Web.services.CloudWatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CloudWatchService cloudWatchService;


    Double get_attachments=0.0;
    Double post_attachment=0.0;
    Double put_attachment=0.0;
    Double delete_attachment=0.0;


    @GetMapping("/transaction/{id}/attachments")
    public ResponseEntity<Object> getAttachments(@PathVariable(value = "id") long id)
    {


        cloudWatchService.putMetricData("GetRequest","/transaction/{id}/attachments",++get_attachments);

        Optional<Transaction> transaction=transactionRepository.findById(id);

        if(!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok().body(transaction.get().getAttachments());
        }

    }


    @PostMapping("/transaction/{id}/attachment")
    public ResponseEntity<Object> postNewAttachment(@RequestBody Receipt receipt,@PathVariable long id)
    {


        cloudWatchService.putMetricData("PostRequest","/transaction/{id}/attachment",++post_attachment);

        Optional<Transaction> transaction=transactionRepository.findById(id);

        if(!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            try {
                receipt.setTransaction(transaction.get());
                transaction.get().getAttachments().add(receipt);
                receiptRepository.save(receipt);
                return ResponseEntity.ok().body(receipt);
            }catch (Exception e)
            {
                return ResponseEntity.badRequest().body(e);
            }
        }

    }

    @PutMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> addNewAttachment(@RequestBody Receipt receipt,@PathVariable(value="id") long id ,@PathVariable(value="attachmentID") long attachID)
    {

        cloudWatchService.putMetricData("PutRequest","/transaction/{id}/attachment/{attachmentID}",++put_attachment);


        Optional<Transaction> transaction=transactionRepository.findById(id);
        Optional<Receipt> old_receipt=receiptRepository.findById(attachID);

        if(!transaction.isPresent() || !old_receipt.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
         receipt.setId(attachID);
         receipt.setTransaction(transaction.get());
         receiptRepository.save(receipt);
         return ResponseEntity.ok().body(receipt);
        }



    }

    @DeleteMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> deleteAttachment(@PathVariable(value = "id") long id, @PathVariable(value="attachmentID") long attachID)
    {

        cloudWatchService.putMetricData("DeleteRequest","/transaction/{id}/attachment/{attachmentID}",++delete_attachment);

        Optional<Transaction> transaction= transactionRepository.findById(id);
        Optional<Receipt>     receipt=receiptRepository.findById(attachID);

        if(!transaction.isPresent()||!receipt.isPresent())
        {
            return ResponseEntity.notFound().build();

        }
        else
        {
            transaction.get().getAttachments().remove(receipt.get());
            receiptRepository.deleteById(attachID);
            return ResponseEntity.noContent().build();
        }

    }


}
