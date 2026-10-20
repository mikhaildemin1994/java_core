package org.management.app.auditlog.aspect;

import org.management.app.auditlog.model.AuditLog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditAnnotation {
    AuditLog.Action action();
}
