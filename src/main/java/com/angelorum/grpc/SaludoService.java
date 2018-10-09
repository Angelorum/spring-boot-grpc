package com.angelorum.grpc;

import com.angelorum.grpc.saludo.HelloReply;
import com.angelorum.grpc.saludo.HelloRequest;
import com.angelorum.grpc.saludo.SaludoServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.security.SecureRandom;
import java.util.stream.IntStream;

@Slf4j
@GRpcService
public class SaludoService extends SaludoServiceGrpc.SaludoServiceImplBase{

    @Override
    public void saludando(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        SecureRandom random = new SecureRandom();
        int i = random.nextInt(200);
        log.info("Generando {} mensajes", i);

        IntStream.rangeClosed(1, i).forEach(value -> {
            try {
                //Simulamos operacion tardada
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                //En caso de error
                responseObserver.onError(e);
            }
            //Send event!
            responseObserver.onNext(HelloReply.newBuilder()
                    .setMessage(String.format("Hola %s. #%d de %d", request.getName(), value, i))
                    .build());
        });

        //Terminate streaming
        responseObserver.onCompleted();
    }
}
