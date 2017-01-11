package io.vertx.servicediscovery.polyglot;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.redis.RedisClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.service.HelloService;
import io.vertx.servicediscovery.types.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MyVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
    EventBus eb = vertx.eventBus();

    eb.consumer("http-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-http-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            HttpClient client = reference.get();
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("http-sugar", message -> {
      JsonObject result = new JsonObject();
      HttpEndpoint.getClient(discovery,
        record -> record.getName().equalsIgnoreCase("my-http-service"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            HttpClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("service-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            HelloService client = reference.get();
            result.put("client", client.toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("service-sugar", message -> {
      JsonObject result = new JsonObject();
      EventBusService.getServiceProxy(discovery,
        record -> record.getName().equalsIgnoreCase("my-service"),
        HelloService.class,
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            HelloService client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("ds-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            JDBCClient client = reference.get();
            result.put("client", client.toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("ds-sugar", message -> {
      JsonObject result = new JsonObject();
      JDBCDataSource.getJDBCClient(discovery, record -> record.getName().equalsIgnoreCase("my-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            JDBCClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("redis-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-redis-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            RedisClient client = reference.get();
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("redis-sugar", message -> {
      JsonObject result = new JsonObject();
      RedisDataSource.getRedisClient(discovery, record -> record.getName().equalsIgnoreCase("my-redis-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            RedisClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("mongo-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-mongo-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            MongoClient client = reference.get();
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("mongo-sugar", message -> {
      JsonObject result = new JsonObject();
      MongoDataSource.getMongoClient(discovery,
        record -> record.getName().equalsIgnoreCase("my-mongo-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            MongoClient client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });

    eb.consumer("source1-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-message-source-1"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            MessageConsumer client = reference.get();
            result.put("client", client.toString());
            result.put("direct", reference.get().toString());
            message.reply(result);
          }
        }
      }));

    eb.consumer("source1-sugar", message -> {
      JsonObject result = new JsonObject();
      MessageSource.<String>getConsumer(discovery, record -> record.getName().equalsIgnoreCase("my-message-source-1"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            MessageConsumer<String> client = ar.result();
            result.put("client", client.toString());
            message.reply(result);
          }
        });
    });


  }
}
