from langchain.embeddings.base import Embeddings

class FakeEmbeddings(Embeddings):
    def embed_query(self, text: str) -> list[float]:
        return [0.123] * 1536

    def embed_documents(self, texts: list[str]) -> list[list[float]]:
        return [[0.123] * 1536 for _ in texts]
