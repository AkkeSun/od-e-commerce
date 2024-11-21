# pip install pymysql pandas sentence-transformers chromadb
from sentence_transformers import SentenceTransformer
import pymysql
import pandas as pd
import chromadb
import uuid

print("----- product embedding start -----")
print("step1. roading product data from mysql")
# -------------- step 1. mysql 데이터베이스에서 상품 정보 가져오기 --------------
connection_shard1 = pymysql.connect(
    host="localhost",
    port=3307,
    user="root",
    password="test",
    database="product",
    charset="utf8mb4"
)
connection_shard2 = pymysql.connect(
    host="localhost",
    port=13307,
    user="root",
    password="test",
    database="product",
    charset="utf8mb4"
)

query = """
          SELECT TABLE_INDEX, PRODUCT_NAME, DESCRIPTION, CATEGORY, PRICE, SELLER_EMAIL
          FROM PRODUCT
          WHERE EMBEDDING_YN = 'N'
        """
df_shard_1 = pd.read_sql(query, connection_shard1)
df_shard_2 = pd.read_sql(query, connection_shard2)
df = pd.concat([df_shard_1, df_shard_2], ignore_index=True)

# -------------- step 2. embedding --------------
print("step2. embedding product data")
model = SentenceTransformer('jhgan/ko-sroberta-multitask')

documents = []
embeddings = []
metadatas = []

for index, row in df.iterrows():
    text = f"""
        이 상품의 이름은 {row["PRODUCT_NAME"]} 이고 {row["CATEGORY"]} 카테고리에 속해 있습니다.
        상품의 가격은 {row["PRICE"]} 입니다.
        {row["DESCRIPTION"]}
      """
    documents.append(text)
    embeddings.append(model.encode(text).tolist())
    metadatas.append(
      {
        "productId": row["TABLE_INDEX"],
        "productName": row["PRODUCT_NAME"],
        "category": row["CATEGORY"],
        "price": row["PRICE"],
        "sellerEmail": row["SELLER_EMAIL"]
      }
    )

# -------------- step 3. vector db save --------------
print("step3. save product embedding to chroma")
chroma_client = chromadb.HttpClient(host="localhost", port=8000)
collection = chroma_client.get_or_create_collection(name="test")

batch_size = 100
for i in range(len(embeddings)):
    collection.add(
        ids=[str(uuid.uuid4())],
        embeddings=[embeddings[i]],  # 하나의 임베딩만 추가
        metadatas=[metadatas[i]],    # 하나의 메타데이터만 추가
        documents=[documents[i]]     # 하나의 문서만 추가
    )

print("----- product embedding start -----")