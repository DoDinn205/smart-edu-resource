import { useCallback, useRef, useState } from "react";

const useSubmissionGuard = () => {
    const lockRef = useRef(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const runSubmission = useCallback(async (action) => {
        if (lockRef.current) return false;

        lockRef.current = true;
        setIsSubmitting(true);
        try {
            await action();
            return true;
        } finally {
            lockRef.current = false;
            setIsSubmitting(false);
        }
    }, []);

    return { isSubmitting, runSubmission };
};

export default useSubmissionGuard;
