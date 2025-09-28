FROM nginx:latest

COPY proxy/nginx.conf /etc/nginx/nginx.conf
COPY proxy/conf.d/ /etc/nginx/conf.d/

CMD ["nginx", "-g", "daemon off;"]
