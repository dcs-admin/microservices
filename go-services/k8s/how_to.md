# How to build and deploy this app

This explains how to build and deploy with useful commands

# Step-1: Build docker images

```
cd /Users/aevana/poc/microservices/go-services

docker build -t auth-service:local ./auth-service
docker build -t product-service:local ./product-service
docker build -t customer-service:local ./customer-service
docker buildx build --platform=linux/amd64 -t order-service:local ./order-service 
docker buildx build --platform=linux/amd64 -t order-dispatcher-service:local ./order-dispatcher-service
docker build -t goui:local ./goui
```

> Incase if you want to delete
```
kubectl delete pod -l app=auth-service
kubectl delete pod -l app=product-service
kubectl delete pod -l app=customer-service
kubectl delete pod -l app=order-service
kubectl delete pod -l app=order-dispatcher-service
```

- Validate whether docker images have been created 
```
docker images
```

Output should be like this 
```
aevana@aevana0425mac go-services % docker images
REPOSITORY                                TAG                                                                           IMAGE ID       CREATED         SIZE
order-service                             local                                                                         641a1e0cdfa1   4 hours ago     39.4MB
order-dispatcher-service                  local                                                                         b33205a25629   2 days ago      142MB
auth-service                              local                                                                         7bd403e827b1   2 days ago      155MB
customer-service                          local                                                                         34bb06081ea4   2 days ago      156MB
product-service                           local                                                                         798a13370c3b   2 days ago      156MB
goui                                      local                                                                         6bc79fd41ff1   2 days ago      75.9MB
bitnami/kafka                             latest                                                                        1479fb510699   6 days ago      731MB
bitnami/kafka                             4.0.0-debian-12-r0                                                            74d23b84870f   2 weeks ago     735MB
docker/desktop-kubernetes                 kubernetes-v1.32.2-cni-v1.6.0-critools-v1.31.1-cri-dockerd-v0.3.16-1-debian   fdd1722efdcc   7 weeks ago     570MB
registry.k8s.io/kube-apiserver            v1.32.2                                                                       c47449f3e751   2 months ago    123MB
registry.k8s.io/kube-controller-manager   v1.32.2                                                                       399aa50f4d13   2 months ago    114MB
registry.k8s.io/kube-scheduler            v1.32.2                                                                       45710d74cfd5   2 months ago    90.1MB
registry.k8s.io/kube-proxy                v1.32.2                                                                       83c025f0faa6   2 months ago    128MB
mysql                                     8.0                                                                           bf577825b52a   2 months ago    1.04GB
busybox                                   latest                                                                        37f7b378a29c   6 months ago    6.02MB
registry.k8s.io/etcd                      3.5.16-0                                                                      c6a9d11cc5c0   7 months ago    213MB
registry.k8s.io/coredns/coredns           v1.11.3                                                                       9caabbf6238b   8 months ago    81.8MB
registry.k8s.io/pause                     3.10                                                                          ee6521f290b2   10 months ago   786kB
docker/desktop-vpnkit-controller          dc331cb22850be0cdd97c84a9cfecaf44a1afb6e                                      7ecf567ea070   23 months ago   44.7MB
docker/desktop-storage-provisioner        v2.0                                                                          115d77efe6e2   3 years ago     56MB
```

# Step-2: Install kafka through HELM(Kubernetes package manager)

```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

helm install my-kafka bitnami/kafka \
  --set replicaCount=1 \
  --set zookeeper.enabled=true \
  --set auth.enabled=false \
  --set listeners.client.protocol=PLAINTEXT


kubectl run my-kafka-client --restart='Never' --image docker.io/bitnami/kafka:4.0.0-debian-12-r0 --namespace default --command -- sleep infinity
kubectl exec --tty -i my-kafka-client --namespace default -- bash

TOPIC:
	kafka-topics.sh --create --bootstrap-server my-kafka-controller-0.my-kafka-controller-headless.default.svc.cluster.local:9092 --replication-factor 1 --partitions 1 --topic order-events

PRODUCER:
    kafka-console-producer.sh \
        --bootstrap-server my-kafka.default.svc.cluster.local:9092 \
        --topic order-events

CONSUMER:
    kafka-console-consumer.sh \
        --bootstrap-server my-kafka.default.svc.cluster.local:9092 \
        --topic order-events \
        --from-beginning
```

# Step-3: Deploy with Kubectl command

```
kubectl apply -f k8s/  --> As a whole

kubectl apply -f k8s/auth-service.yaml
kubectl apply -f k8s/product-service.yaml
kubectl apply -f k8s/customer-service.yaml
kubectl apply -f k8s/order-service.yaml
kubectl apply -f k8s/order-dispatcher-service.yml
kubectl apply -f k8s/goui

```

## Final validation 

```

aevana@aevana0425mac go-services % kubectl get all        
NAME                                            READY   STATUS    RESTARTS        AGE
pod/auth-service-769c64c98c-lk967               1/1     Running   0               2d21h
pod/customer-service-56f8974cd4-9wsjr           1/1     Running   0               2d21h
pod/goui-5d48bc5c58-sg796                       1/1     Running   1               2d22h
pod/my-kafka-client                             1/1     Running   0               16m
pod/my-kafka-controller-0                       1/1     Running   0               19m
pod/my-kafka-controller-1                       1/1     Running   0               19m
pod/my-kafka-controller-2                       1/1     Running   0               19m
pod/mysql-6b84f7c56b-27kz6                      1/1     Running   0               2d22h
pod/order-dispatcher-service-6c8bcd4f47-2njb4   1/1     Running   6 (7m14s ago)   2d20h
pod/order-service-bf7cb64cb-bz4nw               1/1     Running   0               2d20h
pod/product-service-7b7c756d8-g66kz             1/1     Running   0               2d21h

NAME                                   TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
service/auth-service                   NodePort    10.97.198.144    <none>        8888:30001/TCP               3d1h
service/customer-service               NodePort    10.111.252.174   <none>        1000:30002/TCP               2d22h
service/goui                           NodePort    10.105.110.246   <none>        80:32000/TCP                 3d1h
service/kubernetes                     ClusterIP   10.96.0.1        <none>        443/TCP                      3d1h
service/my-kafka                       ClusterIP   10.111.45.176    <none>        9092/TCP                     19m
service/my-kafka-controller-headless   ClusterIP   None             <none>        9094/TCP,9092/TCP,9093/TCP   19m
service/mysql                          NodePort    10.108.165.53    <none>        3306:30123/TCP               2d23h
service/order-dispatcher-service       NodePort    10.98.1.90       <none>        4000:30005/TCP               2d21h
service/order-service                  NodePort    10.97.11.110     <none>        3000:30004/TCP               2d22h
service/product-service                NodePort    10.99.246.40     <none>        2000:30003/TCP               2d23h

NAME                                       READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/auth-service               1/1     1            1           2d22h
deployment.apps/customer-service           1/1     1            1           2d22h
deployment.apps/goui                       1/1     1            1           2d22h
deployment.apps/mysql                      1/1     1            1           2d22h
deployment.apps/order-dispatcher-service   1/1     1            1           2d21h
deployment.apps/order-service              1/1     1            1           2d22h
deployment.apps/product-service            1/1     1            1           2d23h

NAME                                                  DESIRED   CURRENT   READY   AGE
replicaset.apps/auth-service-696bd8c79b               0         0         0       2d22h
replicaset.apps/auth-service-769c64c98c               1         1         1       2d22h
replicaset.apps/customer-service-56f8974cd4           1         1         1       2d22h
replicaset.apps/goui-5d48bc5c58                       1         1         1       2d22h
replicaset.apps/mysql-599b55d68                       0         0         0       2d22h
replicaset.apps/mysql-6b84f7c56b                      1         1         1       2d22h
replicaset.apps/order-dispatcher-service-6c8bcd4f47   1         1         1       2d21h
replicaset.apps/order-service-bf7cb64cb               1         1         1       2d22h
replicaset.apps/product-service-5d87b857bd            0         0         0       2d23h
replicaset.apps/product-service-664c57cfd7            0         0         0       2d23h
replicaset.apps/product-service-76d6c9d464            0         0         0       2d23h
replicaset.apps/product-service-7b7c756d8             1         1         1       2d22h

NAME                                   READY   AGE
statefulset.apps/my-kafka-controller   3/3     19m
aevana@aevana0425mac go-services % 
aevana@aevana0425mac go-services % 
aevana@aevana0425mac go-services % kubectl get serets
error: the server doesn't have a resource type "serets"
aevana@aevana0425mac go-services % kubectl get secrets
NAME                             TYPE                 DATA   AGE
my-kafka-kraft                   Opaque               4      21m
my-kafka-user-passwords          Opaque               2      21m
sh.helm.release.v1.my-kafka.v1   helm.sh/release.v1   1      21m
```