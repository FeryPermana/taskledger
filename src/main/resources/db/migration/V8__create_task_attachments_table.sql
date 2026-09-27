CREATE TABLE task_attachments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100) NULL,
    file_size BIGINT NULL,
    uploaded_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_task_attachments_task
        FOREIGN KEY (task_id)
        REFERENCES tasks(id),

    CONSTRAINT fk_task_attachments_uploaded_by
        FOREIGN KEY (uploaded_by)
        REFERENCES users(id)
);