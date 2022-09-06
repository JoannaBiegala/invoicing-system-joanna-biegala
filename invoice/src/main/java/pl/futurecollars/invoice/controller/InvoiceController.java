package pl.futurecollars.invoice.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoice.db.memory.InMemoryDatabase;
import pl.futurecollars.invoice.model.Invoice;
import pl.futurecollars.invoice.service.InvoiceService;

@RestController
public class InvoiceController {

  private final InvoiceService invoiceService = new InvoiceService(new InMemoryDatabase());

  @GetMapping("/invoices/{id}")
  public ResponseEntity<Invoice> getInvoice(@PathVariable long id) {
    return ResponseEntity.of(Optional.ofNullable(invoiceService.findForId(id)));
  }

  @GetMapping("/invoices")
  public List<Invoice> getAll() {
    return invoiceService.getAll();
  }

  @PostMapping("/invoices")
  public long saveInvoice(@RequestBody Invoice invoice) {
    return invoiceService.saveInvoice(invoice);
  }

  @DeleteMapping("/invoices/{id}")
  public void deleteById(@PathVariable long id) {
    invoiceService.deleteInvoice(id);
  }

  @PutMapping("/invoices/{id}")
  public void update(@PathVariable long id, @RequestBody Invoice invoice) {
    invoiceService.updateInvoice(id, invoice);
  }

}
