package com.felipe.kafka.saga;

import java.util.UUID;

public abstract class BaseSagaTransaction {
  private final UUID sagaId;
  private final UUID transactionId;
  private final Command command;

  public enum Command {
    CREATE,
    COMMIT,
    CANCEL
  }

  public BaseSagaTransaction(UUID sagaId, UUID transactionId, Command command) {
    this.sagaId = sagaId;
    this.transactionId = transactionId;
    this.command = command;
  }

  public UUID getSagaId() {
    return this.sagaId;
  }

  public UUID getTransactionId() {
    return this.transactionId;
  }

  public Command getCommand() {
    return this.command;
  }
}
