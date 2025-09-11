package com.xp.guardian.exception;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        Object message, // Pode ser uma String ou um Map de erros de validação
        String path
) {}