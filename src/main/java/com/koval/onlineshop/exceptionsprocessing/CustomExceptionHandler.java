package com.koval.onlineshop.exceptionsprocessing;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.softlab.efr.common.bpm.support.exceptions.ProcessDefinitionNotFoundException;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.koval.efr.logger.OperationLogEntry;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private OperationLogEntry operationLogEntry;
    private final List<String> errors = new ArrayList<>();

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
        errors.addAll(ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList()));
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
        operationLogEntry.setOperationDescription("handleMethodArgumentNotValid: " + ex.getLocalizedMessage());

        return handleExceptionInternal(ex, errorsMessage, headers, errorsMessage.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
        operationLogEntry.setOperationDescription("handleMissingServletRequestParameter: " + ex.getLocalizedMessage());

        return new ResponseEntity<>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(RuntimeException ex, WebRequest request) {
        errors.add("This argument specification violated. Valid values valid values in any register: ");
        errors.add("for argument column: ");
        errors.add("for argument sort: ");
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
        operationLogEntry.setOperationDescription("handleMethodArgumentTypeMismatch: " + ex.getLocalizedMessage());
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }

    @ExceptionHandler({NullPointerException.class})
        /*500*/ ResponseEntity<Object> handleInternal(final Exception ex, final WebRequest request) {
        errors.add(ex.getMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        operationLogEntry.setOperationDescription("handleInternal: " + ex.getLocalizedMessage());
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        final String bodyOfResponse = "This should be application specific";
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
     protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        errors.add(ex.getMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        operationLogEntry.setOperationDescription("handleNotFound: " + ex.getLocalizedMessage());
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }

    /*
    @ExceptionHandler({NoSuchRequestHandlingMethodException.class, HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class, MissingPathVariableException.class, MissingServletRequestParameterException.class, ServletRequestBindingException.class, ConversionNotSupportedException.class, TypeMismatchException.class, HttpMessageNotReadableException.class, HttpMessageNotWritableException.class, MethodArgumentNotValidException.class, MissingServletRequestPartException.class, BindException.class, NoHandlerFoundException.class, AsyncRequestTimeoutException.class})
    public  ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) throws Exception {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        operationLogEntry.write();
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }*/
    @ExceptionHandler({Exception.class, IOException.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        errors.add(ex.getLocalizedMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }
    @ExceptionHandler({EntityExistsException.class, ProcessDefinitionNotFoundException.class})
    public ResponseEntity<Object> handleExistsException(Exception ex, WebRequest request) {
        errors.add(ex.getLocalizedMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }

    @ExceptionHandler({MailException.class, MessagingException.class})
    public ResponseEntity<Object> handleMailExistsException(Exception ex, WebRequest request) {
        errors.add(ex.getLocalizedMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }

    @ExceptionHandler({SQLException.class, ClassNotFoundException.class})
    public ResponseEntity<Object> handleSQLExistsException(Exception ex, WebRequest request) {
        errors.add(ex.getLocalizedMessage());
        operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
        operationLogEntry.setOperationDescription(ex.getLocalizedMessage());
        ErrorsMessage errorsMessage = new ErrorsMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(errorsMessage, new HttpHeaders(), errorsMessage.getStatus());
    }
}
