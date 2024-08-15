package capstone.allbom.chatbot.dto;

import capstone.allbom.chatbot.domain.Qna;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대화 내역 쌍")
public record QnaPair(
        @Schema(description = "질문", example = "비행기야?")
        String question,

        @Schema(description = "답변", example = "아니요, 비행기가 아닙니다.")
        String answer
) {
    public static QnaPair from(Qna qna) {
        return new QnaPair(
                qna.getQuestion(), qna.getAnswer()
        );
    }
}
