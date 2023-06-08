kubectl proxy
kubectl -n kubernetes-dashboard create token admin-user
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

helm -n lksg-tool-irs template lksg-tool-irs -s templates\ingress.yaml .
helm -n lksg-tool-irs install lksg-tool-irs .

kubectl delete -n lksg-tool persistentvolumeclaim data-lksg-tool-postgresql-0

lksg-tool-postgresql.lksg-tool.svc.cluster.local

k -n lksg-tool get pods -o=custom-columns=NAME:.metadata.name,IP:.status.podIP

helm -n lksg-tool uninstall lksg-tool-irs
kubectl delete -n lksg-tool persistentvolumeclaim data-lksg-tool-postgresql-0
helm -n lksg-tool install lksg-tool-irs  .

k exec busybox -- nslookup kubernetes.default
