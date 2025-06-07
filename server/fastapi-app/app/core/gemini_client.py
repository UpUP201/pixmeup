import asyncio
import os
import google.generativeai as genai

genai.configure(api_key=os.getenv("GEMINI_API_KEY"))
USE_MOCK = os.getenv("USE_MOCK", "false").lower() == "true"

model = genai.GenerativeModel("models/gemini-2.0-flash")

async def get_gemini_response(prompt: str) -> str:
    if USE_MOCK:
        await asyncio.sleep(0.01)
        return "이것은 모의 응답입니다."
    else:
        response = model.generate_content(prompt)
        return response.text

async def get_gemini_embedding(text: str) -> list[float]:
    response = genai.embed_content(
        model="models/embedding-001",
        content=text
    )
    return response["embedding"]